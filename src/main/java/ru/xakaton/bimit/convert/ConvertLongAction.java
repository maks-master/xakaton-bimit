package ru.xakaton.bimit.convert;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

//import ru.xakaton.bimit.enums.ProcessType;
import ru.xakaton.bimit.enums.StateProcess;
import ru.xakaton.bimit.event.RevisionNotificationEvent;
import ru.xakaton.bimit.model.ProcessState;
import ru.xakaton.bimit.repository.ProcessRepository;
import ru.xakaton.bimit.model.Model;
import ru.xakaton.bimit.storage.StorageService;
import ru.xakaton.bimit.convert.threads.LongActionThread;
//import ru.xakaton.bimit.convert.threads.enums.ThreadQueueObject;
import ru.xakaton.bimit.convert.threads.model.ThreadQueue;
import ru.xakaton.bimit.convert.threads.repository.ThreadQueueRepository;
import ru.xakaton.bimit.convert.threads.repository.ThreadRegisterRepository;

@Service
@Scope("prototype")
public class ConvertLongAction extends LongAction {
	private static Logger log = LoggerFactory.getLogger(ConvertLongAction.class);

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Value("${convertersPath}")
	private String converterPath;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ProcessRepository processRepository;

	@Autowired
	ThreadRegisterRepository threadRegisterRepository;

	@Autowired
	ThreadQueueRepository threadQueueRepository;

	@Override
	public void unRegisterProcess() {
		threadQueueRepository.findByModelUuid(getProcessState().getModel().getUuid())
			.ifPresent(threadQueue -> {
				threadQueueRepository.delete(threadQueue);
		});
	}

	@Override
	public void registerProcess() {
		UUID threadUuid = LongActionThread.THREAD_UUID;

		threadRegisterRepository.findByThreaduuid(threadUuid).ifPresent(threadRegister -> {
			Integer maxSort = threadQueueRepository.maxSortValue();
			int lastIndex = maxSort != null ? (maxSort + 1) : 0;
			ThreadQueue threadQueue = new ThreadQueue(lastIndex, 
					threadRegister, getProcessState().getModel().getUuid());
			threadQueueRepository.save(threadQueue);
		});
	}

	@Override
	public void createProcess(Model model) {
		ProcessState processState = new ProcessState();
		processState.setStartDate(new Timestamp(new Date().getTime()));
		processState.setModel(model);
		processState.setStateProcess(StateProcess.IDLE);

		setProcessState(processRepository.save(processState));

		registerProcess();
	}

	@Override
	public void updateProcess(StateProcess stateProcess, double progress) {
		ProcessState processState = getProcessState();
		processState.setProgress(progress);
		processState.setStateProcess(stateProcess);
		setProcessState(processRepository.save(processState));

		notificationEvent();
	}

	@Override
	public void updateProcess(StateProcess stateProcess) {
		updateProcess(stateProcess, "");
	}

	@Override
	public void updateProcess(StateProcess stateProcess, String info) {
		ProcessState processState = getProcessState();
		processState.setStateProcess(stateProcess);
		processState.setInfo(info);
		setProcessState(processRepository.save(processState));

		notificationEvent();
	}

	public void notificationEvent() {
		ProcessState processState = getProcessState();
		if (processState.getStateProcess().equals(StateProcess.FINISHED_SUCCESS)) {
			Model model = processState.getModel();
			applicationEventPublisher
					.publishEvent(new RevisionNotificationEvent(this, model, "CONVERT_FINISHED_SUCCESS"));

			unRegisterProcess();
		} else if (processState.getStateProcess().equals(StateProcess.FINISHED_ERROR)) {
			Model model = processState.getModel();
			applicationEventPublisher
					.publishEvent(new RevisionNotificationEvent(this, model, "CONVERT_FINISHED_ERROR"));

			unRegisterProcess();
		}
	}

	@Override
	public void run() {
		log.info("START CONVERTING ACTION");

		final Process longProcess;
		Process process;

		Model modelRevision = getProcessState().getModel();

		updateProcess(StateProcess.RUNNING, 10.0);

		try {
			Resource resource = storageService.loadAsResource(modelRevision.getFile());
			String filePath = resource.getFile().getAbsolutePath();

			long startProc = System.currentTimeMillis();
			
			log.info("IFC to DAE");
			ProcessBuilder builder = new ProcessBuilder();

			String daeFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".dae";
			int coresForGeometryGeneration = 1;
			coresForGeometryGeneration = Runtime.getRuntime().availableProcessors();
			if (coresForGeometryGeneration>3 && coresForGeometryGeneration < 8) {
				coresForGeometryGeneration = coresForGeometryGeneration - 1;
			} else
			if (coresForGeometryGeneration >= 8) {
				// Machines with > 8 cores will automatically free up one core per 8
				// (additional) cores for non-geometry generation (BIMserver itself)
				// For example a 48 core machine will free up 6 cores for BIMserver, an 8 core
				// machine will use 7 cores for geometry generation
				// Any machine with < 8 cores will use all cores for geometry generation
				coresForGeometryGeneration = ((coresForGeometryGeneration * 7) / 8);
			}

			if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
				builder.command("cmd.exe", "/c", "IfcConvert  -j "+coresForGeometryGeneration+" -y --use-element-guids " + filePath + " " + daeFilePath
						+ " --exclude=entities IfcNesushestvuyushiiElement");
			} else {
				builder.command("sh", "-c", this.converterPath + "/IfcConvert/IfcConvert -j "+coresForGeometryGeneration+" -y --use-element-guids "
						+ filePath + " " + daeFilePath + " --exclude=entities IfcNesushestvuyushiiElement");
			}

			builder.directory(new File(this.converterPath + "/IfcConvert"));

			longProcess = builder.start();

			//final double percents = 10.0;
			double startPercents = 10.0;
			final Thread ioThread = new Thread() {
			    @Override
			    public void run() {
			        try {
			            final BufferedReader reader = new BufferedReader(
			                    new InputStreamReader(longProcess.getInputStream()));
			            String line = null;
			            while ((line = reader.readLine()) != null) {
			            	if (line.contains("[")) {//50
			            		long count = line.chars().filter(ch -> ch == '#').count();
			            		double percents = startPercents + count*4.0/5.0;
			            		
			            		updateProcess(StateProcess.RUNNING, percents);
			            	}
			            }
			            reader.close();
			        } catch (final Exception e) {
			            e.printStackTrace();
			        }
			    }
			};
			ioThread.start();
			
			/*StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), process.getErrorStream(),
					log::info);
			Executors.newSingleThreadExecutor().submit(streamGobbler);*/
			
			
			int exitCode = longProcess.waitFor();
			
			log.info("IFC to DAE Exit Code " + exitCode +" in " + (System.currentTimeMillis() - startProc) + " мс");
			startProc = System.currentTimeMillis();
			// assert exitCode == 0 : "IFC to DAE Error Transform";
			if (exitCode != 0) 
				throw new RuntimeException("IFC to DAE Error Transform in " + (System.currentTimeMillis() - startProc) + " мс ");

			updateProcess(StateProcess.RUNNING, 50.0);

			log.info("DAE to GLTF");
			builder = new ProcessBuilder();

			String gltfFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".gltf";

			if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
				builder.command("cmd.exe", "/c",
						"COLLADA2GLTF-bin.exe -v -i " + daeFilePath + " -o " + gltfFilePath + "");
			} else {
				builder.command("sh", "-c", this.converterPath + "/COLLADA2GLTF/COLLADA2GLTF-bin -v -i " + daeFilePath
						+ " -o " + gltfFilePath + "");
			}

			builder.directory(new File(this.converterPath + "/COLLADA2GLTF"));

			process = builder.start();

			StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), process.getErrorStream(), log::info);
			Executors.newSingleThreadExecutor().submit(streamGobbler);
			exitCode = process.waitFor();
			log.info("DAE to GLTF Exit Code " + exitCode + " in " + (System.currentTimeMillis() - startProc) + " мс");
			startProc = System.currentTimeMillis();
			if (exitCode != 0)
				throw new RuntimeException("DAE to GLTF Error Transform in " + (System.currentTimeMillis() - startProc) + " мс");
			// assert exitCode == 0 : "DAE to GLTF Error Transform";

			File f = new File(daeFilePath);
			f.delete();

			updateProcess(StateProcess.RUNNING, 70.0);

			log.info("GLTF to XKT");
			builder = new ProcessBuilder();

			String xktFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".xkt";

			if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
				builder.command("cmd.exe", "/c", "node gltf2xkt.js -s " + gltfFilePath + " -o " + xktFilePath + "");
			} else {
				builder.command("sh", "-c", "node " + this.converterPath + "/gltf2xkt/gltf2xkt.js -s " + gltfFilePath
						+ " -o " + xktFilePath + "");
			}

			builder.directory(new File(this.converterPath + "/gltf2xkt"));

			process = builder.start();

			streamGobbler = new StreamGobbler(process.getInputStream(), process.getErrorStream(), log::info);
			Executors.newSingleThreadExecutor().submit(streamGobbler);
			exitCode = process.waitFor();
			log.info("GLTF to XKT Exit Code " + exitCode + " in " + (System.currentTimeMillis() - startProc) + " мс");
			startProc = System.currentTimeMillis();
			if (exitCode != 0)
				throw new RuntimeException("GLTF to XKT Error Transform  in " + (System.currentTimeMillis() - startProc) + " мс");
			// assert exitCode == 0 : "GLTF to XKT Error Transform";

			updateProcess(StateProcess.RUNNING, 85.0);

			f = new File(gltfFilePath);
			f.delete();

			f = new File(xktFilePath);

			modelRevision.setXktFile(f.getName());

			log.info("Get JSON METAMODEL");
			builder = new ProcessBuilder();

			String jsonFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".json";

			if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
				builder.command("cmd.exe", "/c", "xeokit-metadata " + filePath + " " + jsonFilePath + "");
			} else {
				builder.command("sh", "-c",
						this.converterPath + "/xeokit-metadata/xeokit-metadata " + filePath + " " + jsonFilePath + "");
			}

			builder.directory(new File(this.converterPath + "/xeokit-metadata"));

			process = builder.start();

			streamGobbler = new StreamGobbler(process.getInputStream(), process.getErrorStream(), log::info);
			Executors.newSingleThreadExecutor().submit(streamGobbler);
			exitCode = process.waitFor();
			log.info("Get JSON METAMODEL Exit Code " + exitCode + " in " + (System.currentTimeMillis() - startProc) + " мс");
			startProc = System.currentTimeMillis();
			// assert exitCode == 0 : "Get JSON METAMODEL Error";
			if (exitCode != 0)
				throw new RuntimeException("Get JSON METAMODEL Error in " + (System.currentTimeMillis() - startProc) + " мс");

			updateProcess(StateProcess.RUNNING, 90.0);

			f = new File(jsonFilePath);

			modelRevision.setJsonFile(f.getName());

			log.info("file " + modelRevision.getFile());
			log.info("getXktFile " + modelRevision.getXktFile());
			log.info("getJsonFile " + modelRevision.getJsonFile());

			applicationEventPublisher.publishEvent(new RevisionNotificationEvent(this, modelRevision, "CONVERT_FILES"));

			updateProcess(StateProcess.FINISHED_SUCCESS, 100.0);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			log.error("", e);
			updateProcess(StateProcess.FINISHED_ERROR, sw.toString());
		}

		log.info("END CONVERTING ACTION");
	}
	
}
