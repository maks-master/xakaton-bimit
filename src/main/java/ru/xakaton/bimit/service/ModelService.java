package ru.xakaton.bimit.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.xakaton.bimit.device.model.Alarm;
import ru.xakaton.bimit.device.model.Device;
import ru.xakaton.bimit.device.model.DeviceState;
import ru.xakaton.bimit.device.model.DeviceStateDTO;
import ru.xakaton.bimit.device.repository.AlarmRepository;
import ru.xakaton.bimit.device.repository.DeviceRepository;
import ru.xakaton.bimit.device.repository.DeviceStateRepository;
import ru.xakaton.bimit.device.service.DeviceService;
import ru.xakaton.bimit.model.Model;
import ru.xakaton.bimit.model.ProcessState;
import ru.xakaton.bimit.repository.ModelRepository;
import ru.xakaton.bimit.storage.StorageService;

@Service
public class ModelService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private ModelRepository modelRepository;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	DeviceRepository deviceRepository;
	
	@Autowired
	DeviceStateRepository deviceStateRepository;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	AlarmRepository alarmRepository;
	
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
	@PostConstruct
	public void setupMapper() {
		//modelMapper.createTypeMap(CollisionRuleFinderExportDTO.class, CollisionRuleFinder.class).setPostConverter(toCollisionRuleFinder());
		
		modelMapper.createTypeMap(DeviceState.class, DeviceStateDTO.class)
			.addMappings(m -> m.skip(DeviceStateDTO::setColor))
			.setPostConverter(toDeviceStateDTOConverter());
	}
	
	private Converter<DeviceState, DeviceStateDTO> toDeviceStateDTOConverter() {
		return context -> {
			DeviceState source = context.getSource();
			DeviceStateDTO destination = context.getDestination();
			String color="#DEDEDE;";
			
			if (source.getAlarmUuid()!=null) { 
				Alarm alarm = alarmRepository.findById(source.getAlarmUuid()).get();
				if (alarm!=null) {
					switch (alarm.getAlarmLevel()) {
					case WARNING:
						//желтый
						color="255,255,51";
						break;
					case CRITICAL:
						//фиолетовый
						color="153,0,204";
						break;
					case ERROR:
						//оранжевый
						color="255,102,0";//255,102,0
						break;
					case STRONG:
						//красный
						color="255,51,0";//255,51,0
						break;
					default:
						break;
					}
				}
			} else {
				Device device = deviceRepository.findById(source.getDeviceUuid()).get();
				if (device != null) {
					double avg = source.getAverage();
					if (device.getMaxValue()!=null && device.getMinValue()!=null) {
						
						int val = (int) (150 + (avg-device.getMinValue()) * 100/(device.getMaxValue() - device.getMinValue()));
						
						//00FF33	00FF00	00CC00	33CC33	00CC33
						//155-250
						color="0,"+val+",51";
					}
				}
			}
			destination.setColor(color);
			return destination;
		};
	}

	public List<DeviceStateDTO> getStateDevices() {
		return deviceService.list().parallelStream().map(device -> deviceStateRepository.findFirstByDeviceUuid(device.getUuid()).get()).map(statte -> modelMapper.map(statte, DeviceStateDTO.class)).collect(Collectors.toList());
	}

	public ResponseEntity<?> readState(UUID uuid) {
		DeviceState deviceState = deviceStateRepository.findById(uuid).orElse(null);
		if (deviceState != null) {deviceState.setAlarmUuid(null);
			deviceStateRepository.save(deviceState);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
