package ru.xakaton.bimit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.xakaton.bimit.model.ProcessState;
import ru.xakaton.bimit.model.ProcessStateDTO;
import ru.xakaton.bimit.service.ProcessService;

@RestController
@RequestMapping("/process")
public class ProcessController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	ProcessService processService;

	@PostMapping("/")
	@Transactional(readOnly = true)
	public List<ProcessStateDTO> getMyProcess(@RequestBody UUID[] procs, HttpSession session) {
		List<ProcessStateDTO> listProcesses = new ArrayList<ProcessStateDTO>();
		if (procs == null || procs.length == 0) {
			listProcesses = processService.getMyProcess();
		} else {
			listProcesses = processService.getMyProcess(procs);
		}
		return listProcesses;
	}

	@GetMapping("/all")
	public List<ProcessState> getMyAllProcess() {
		return processService.getMyAllProcess();
	}

	@GetMapping("/{uid}")
	public ProcessState getProcess(@PathVariable UUID uid) {
		return processService.getProcess(uid);
	}
}
