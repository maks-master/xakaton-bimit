package ru.xakaton.bimit.device.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.xakaton.bimit.device.model.Alarm;
import ru.xakaton.bimit.device.model.AlarmTimeLine;
import ru.xakaton.bimit.device.model.Device;
import ru.xakaton.bimit.device.service.DeviceService;

import ru.xakaton.bimit.device.enums.DeviceState;
import ru.xakaton.bimit.device.enums.DeviceType;

@RestController
public class DeviceController {

	@Autowired
	DeviceService deviceService;
	
	@GetMapping("/devices")
	public List<Device> devices(){
		return deviceService.list();
	}
	
	@PostMapping("/devices")
	public Device add(@RequestBody Device device) {
		return deviceService.save(device);
	}
	
	@PutMapping("/devices")
	public Device edit(@RequestBody Device device) {
		return deviceService.save(device);
	}
	
	@DeleteMapping("/devices/{uuid}")
	public void delete(@PathVariable UUID uuid) {
		deviceService.delete(uuid);
	}
	
	@GetMapping("/device/{type}/alarms") 
	public AlarmTimeLine getAlarms(@PathVariable int type) {
		return deviceService.getAlarms(type, null);
	}
	
	
	@GetMapping("/device/{type}/alarms/{time}") 
	public AlarmTimeLine getAlarms(@PathVariable int type, @PathVariable Long time) {
		return deviceService.getAlarms(type, time);
	}
	
	@GetMapping("/device/alarms") 
	public AlarmTimeLine getAlarms() {
		return deviceService.getAlarms(null);
	}
	
	@GetMapping("/device/alarms/{time}") 
	public AlarmTimeLine getAlarms(@PathVariable Long time) {
		return deviceService.getAlarms(time);
	}
	
	/*@GetMapping("/device/alarm/{uuid}") 
	public Alarm getAlarms(@PathVariable UUID uuid) {
		return deviceService.getAlarm(uuid);
	}*/
	
}
