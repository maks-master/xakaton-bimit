package ru.xakaton.bimit.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import ru.xakaton.bimit.convert.ConvertLongAction;
import ru.xakaton.bimit.convert.LongAction;
import ru.xakaton.bimit.enums.StateProcess;
import ru.xakaton.bimit.model.Model;
import ru.xakaton.bimit.model.ProcessState;
import ru.xakaton.bimit.model.ProcessStateDTO;
import ru.xakaton.bimit.repository.ProcessRepository;


@Service
public class ProcessService {

	private static Logger log = LoggerFactory.getLogger(ProcessService.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ProcessRepository processRepository;
	
	@Autowired
	private ApplicationContext applicationContext;

	private TaskExecutor longActionTaskExecutor = new SimpleAsyncTaskExecutor();

	@PostConstruct
	public void setupMapper() {
		modelMapper.createTypeMap(ProcessState.class, ProcessStateDTO.class)
				.addMappings(m -> m.skip(ProcessStateDTO::setName)).setPostConverter(toProcessStateDTOConverter());
	}

	private Converter<ProcessState, ProcessStateDTO> toProcessStateDTOConverter() {
		return context -> {
			ProcessStateDTO destination = context.getDestination();
			destination.setName("Модель успешно конвертирована");
			return destination;
		};
	}

	public ProcessState getProcess(UUID uid) {
		return processRepository.findById(uid).orElseThrow(() -> new RuntimeException("Process not found " + uid));
	}

	public ProcessState addConvertAction(Model model) {
		LongAction longAction = applicationContext.getBean(ConvertLongAction.class);
		longAction.createProcess(model);
		longActionTaskExecutor.execute(longAction);
		return longAction.getProcessState();
	}
	
	public List<ProcessStateDTO> getMyProcess() {
		return processRepository
				.findAllByStateProcessIn(Arrays.asList(StateProcess.IDLE, StateProcess.RUNNING))
				.stream().map(state -> modelMapper.map(state, ProcessStateDTO.class)).collect(Collectors.toList());
	}

	public List<ProcessStateDTO> getMyProcess(UUID[] procs) {
		return processRepository
				.findAllByStateProcessInOrUuidIn(
						Arrays.asList(StateProcess.IDLE, StateProcess.RUNNING), Arrays.asList(procs))
				.stream().map(state -> modelMapper.map(state, ProcessStateDTO.class)).collect(Collectors.toList());
	}

	public List<ProcessState> getMyAllProcess() {
		return processRepository.findAll();
	}


}
