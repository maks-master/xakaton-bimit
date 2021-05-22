package ru.xakaton.bimit.device.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.xakaton.bimit.device.model.Device;
import ru.xakaton.bimit.device.repository.DeviceRepository;

@Service
public class DeviceService {

	private static Logger log = LoggerFactory.getLogger(DeviceService.class);
	
	@Autowired
	DeviceRepository deviceRepository;
	
	public List<Device> list() {
		return (List<Device>) deviceRepository.findAll();
	}

	public Device save(Device device) {
		return deviceRepository.save(device);
	}

	public void delete(UUID uudi) {
		deviceRepository.deleteById(uudi);		
	}
}
