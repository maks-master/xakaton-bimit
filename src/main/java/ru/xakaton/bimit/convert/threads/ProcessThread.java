package ru.xakaton.bimit.convert.threads;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.xakaton.bimit.convert.threads.model.ThreadRegister;
import ru.xakaton.bimit.convert.threads.repository.ThreadQueueRepository;
import ru.xakaton.bimit.enums.StateProcess;
import ru.xakaton.bimit.model.ProcessState;
import ru.xakaton.bimit.repository.ProcessRepository;

@Service
@Scope("prototype")
public class ProcessThread implements Runnable {

	public Logger log = LoggerFactory.getLogger(this.getClass().getName());
	public ThreadRegister threadRegister;

	public ProcessThread(ThreadRegister threadRegister) {
		this.threadRegister = threadRegister;
	}

	Set<ProcessState> forStop = new HashSet<ProcessState>();

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	ThreadQueueRepository threadQueueRepository;

	@Transactional
	public void initProcess() throws Exception {

		processRepository.findAllByStateProcessIn(Arrays.asList(StateProcess.IDLE, StateProcess.RUNNING))
				.forEach(processState -> {
					// проверяем есть ли thread взявший эту задачу
					if (processState.getModel() != null) {
						if (!threadQueueRepository.findByModelUuid(processState.getModel().getUuid()).isPresent()) {
							forStop.add(processState);
						}
					} else
						if (!threadQueueRepository.findByModelUuid(processState.getUuid()).isPresent()) {
							forStop.add(processState);
						}
				});

		forStop.forEach(processState -> {
			processState.setStateProcess(StateProcess.FINISHED_UNKNOWN);
			processRepository.save(processState);
		});
	}

	public void run() {
		long sleeptime = 1;
		try {
			boolean notEndProcess = true;
			while (notEndProcess) {
				Thread.sleep(sleeptime * 1000L);
				try {

					initProcess();

					sleeptime = 60;

				} catch (InterruptedException inex) {
					// Restore the interrupted status
					Thread.currentThread().interrupt();
				} catch (Exception sst) {
					log.error("ProcessThread cicle thread error", sst);
				}
			}
		} catch (Exception e) {
			log.error("ProcessThread main thread error", e);
		}
	}

	public void interrupt() {
		Thread.currentThread().isInterrupted();
	}
}
