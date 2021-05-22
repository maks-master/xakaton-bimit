package ru.xakaton.bimit.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import ru.xakaton.bimit.enums.StateProcess;
import ru.xakaton.bimit.model.ProcessState;


public interface ProcessRepository extends CrudRepository<ProcessState, UUID> {

	List<ProcessState> findAllByStateProcessIn(List<StateProcess> states);

	List<ProcessState> findAllByStateProcessInOrUuidIn(
			List<StateProcess> states, List<UUID> uuids);
	
	List<ProcessState> findAll();
}
