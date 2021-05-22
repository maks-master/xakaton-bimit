package ru.xakaton.bimit.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.xakaton.bimit.model.Model;
import ru.xakaton.bimit.model.ProcessState;
import ru.xakaton.bimit.repository.ModelRepository;
import ru.xakaton.bimit.storage.StorageService;

@Service
public class ModelService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private ModelRepository modelRepository;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ProcessService processService;
	
	public Model loadModel(UUID uid) {
		return modelRepository.findById(uid)
				.orElseThrow(() -> new RuntimeException("Model not found " + uid));
	}
	
	public synchronized void save(Model model, String eventAction) {
		UUID modelUUID = model.getUuid();
		Model modelBase = modelRepository.findById(modelUUID)
				.orElseThrow(() -> new RuntimeException("Model not found " + modelUUID));
		switch (eventAction) {
			
			case "CONVERT_FILES":
				modelBase.setFile(model.getFile());
				modelBase.setXktFile(model.getXktFile());
				modelBase.setJsonFile(model.getJsonFile());
				break;
			default:
				new RuntimeException("Unknown Action");
				break;
		}

		modelRepository.save(modelBase);
	}
	
	
	public Map<String, Object> addModel(Map<String, Object> allParams, MultipartFile file) throws IOException {
		Model model = new Model();
		
		ProcessState processState = null;

		if (file != null) {
			String fName = "model_" + model.getUuid()
					+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

			LOGGER.debug("Add model file name: " + fName);
			storageService.store(fName, file.getInputStream());

			model.setFile(fName);
			modelRepository.save(model);

			processState = processService.addConvertAction(model);
		}

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("Model", model);
		m.put("ProcessState", processState);
		return m;
	}
	
	/*public ResponseEntity<Resource> fileResponseModelRevision(UUID uid) {
		ModelRevision modelRevision = modelRevisionRepository.findById(uid).orElseThrow(() -> new RuntimeException("ModelRevision " + uid + " not found"));
		String filePath = PathService.getModelRevisionPath(modelRevision);
		Resource file = storageService.loadAsResource(filePath);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	public ModelRevision loadModel(UUID uid) {
		return modelRevisionRepository.findById(uid)
				.orElseThrow(() -> new RuntimeException("ModelRevision not found " + uid));
	}
	
	*/
	
}
