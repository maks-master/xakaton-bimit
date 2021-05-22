package ru.xakaton.bimit.device.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.xakaton.bimit.device.enums.DeviceType;
import ru.xakaton.bimit.device.model.Alarm;
import ru.xakaton.bimit.device.model.AlarmDTO;
import ru.xakaton.bimit.device.model.AlarmTimeLine;
import ru.xakaton.bimit.device.model.Device;
import ru.xakaton.bimit.device.model.DeviceData;
import ru.xakaton.bimit.device.repository.AlarmRepository;
import ru.xakaton.bimit.device.repository.DeviceDataRepository;
import ru.xakaton.bimit.device.repository.DeviceRepository;

@Service
public class DeviceService {

	private static Logger log = LoggerFactory.getLogger(DeviceService.class);
	
	@Autowired
	AlarmRepository alarmRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	DeviceRepository deviceRepository;
	
	@Autowired
	DeviceDataRepository deviceDataRepository;
	
	public List<Device> list() {
		return (List<Device>) deviceRepository.findAll();
	}

	public Device save(Device device) {
		return deviceRepository.save(device);
	}

	public void delete(UUID uudi) {
		deviceRepository.deleteById(uudi);		
	}

	public List<AlarmDTO> getAlarms(UUID uuid, Long ts) {
		if (ts == null) {
			ts = new Date().getTime() - 5*60*1000L;
		}
		List<Alarm> alarms = alarmRepository.findAllByDeviceUuidAndTimeAfterOrderByTime(uuid, new Timestamp(ts));
		List<AlarmDTO> alarmsDTO = new ArrayList<AlarmDTO>();
		AlarmDTO alDto = new AlarmDTO();
		
		for (Alarm alarm: alarms) {
			boolean newA = false;
			if (!alarmsDTO.isEmpty()) {
				AlarmDTO lastAlarm = alarmsDTO.get(alarmsDTO.size()-1);
				if (!lastAlarm.getAlarmLevel().equals(alarm.getAlarmLevel())) {
					newA = true;
				} else {
					if (lastAlarm.getEndTime().after(new Timestamp(alarm.getTime().getTime()-100L))) {
						DeviceData data = deviceDataRepository.findById(alarm.getDeviceDataUuid()).orElse(null);
						if (data != null) {
							lastAlarm.setEndTime(new Timestamp(data.getTime().getTime()+data.getCount()*1000L/120L));
						} 
					} else newA = true;
				}
			}
			
			if (alarmsDTO.isEmpty() || newA) {
				alDto = new AlarmDTO();
				alDto.setDeviceDataUuid(alarm.getDeviceDataUuid());
				alDto.setDeviceUuid(alarm.getDeviceUuid());
				alDto.setInfo(alarm.getInfo());
				alDto.setUuid(alarm.getUuid());
				alDto.setAlarmLevel(alarm.getAlarmLevel());
				alDto.setStartTime(alarm.getTime());
				DeviceData data = deviceDataRepository.findById(alarm.getDeviceDataUuid()).orElse(null);
				if (data != null) {
					alDto.setEndTime(new Timestamp(data.getTime().getTime()+data.getCount()*1000L/120L));
				} else {
					alDto.setEndTime(new Timestamp(alarm.getTime().getTime())); 
				}
				alarmsDTO.add(alDto);
			} 
			
			
		}
		
		
	    return alarmsDTO;
	}

	public AlarmTimeLine getAlarms(int devType, Long ts) {
		List<AlarmDTO> alarms = new ArrayList<AlarmDTO>();
		List<Device> devs = deviceRepository.findAllByDeviceType(DeviceType.findValue(""+devType));
		for (Device device: devs) {
			alarms.addAll(getAlarms(device.getUuid(), ts));
		}
		
		return new AlarmTimeLine(alarms, new Date().getTime());
	}
	
	public AlarmTimeLine getAlarms(Long ts) {
		List<AlarmDTO> alarms = new ArrayList<AlarmDTO>();
		List<Device> devs = list();
		
		for (Device device: devs) {
			alarms.addAll(getAlarms(device.getUuid(), ts));
		}
		
		
		return new AlarmTimeLine(alarms, new Date().getTime());
	}

	public Alarm getAlarm(UUID uuid) {
		return alarmRepository.findById(uuid).orElse(null);
	}
}
