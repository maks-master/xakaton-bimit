package ru.xakaton.bimit.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ru.xakaton.bimit.device.model.DeviceStateDTO;
import ru.xakaton.bimit.model.Model;
import ru.xakaton.bimit.service.ModelService;

@RestController
@RequestMapping("/model")
public class ModelController {
	@Autowired
	ModelService modelService;
	
		
	@GetMapping("/{uuid}")
	public Model loadModel(@PathVariable UUID uuid) {
		return modelService.loadModel(uuid);
	}
	
	@PostMapping
	public Map<String, Object> addModel(@RequestParam("modelUuid") UUID modelUuid,
			@RequestParam("file") MultipartFile file,
			@RequestParam Map<String, Object> allParams)
			throws IOException {
		return modelService.addModel(allParams, file);
	}

	
	@GetMapping("/devices/state")
	public List<DeviceStateDTO> getStateDevices() {
		return modelService.getStateDevices();
	}
	
	
	@PatchMapping("/devices/state/{stateUuid}/read")
	public ResponseEntity<?> readState(@PathVariable UUID stateUuid) {
		return modelService.readState(stateUuid);
	}
	

}
