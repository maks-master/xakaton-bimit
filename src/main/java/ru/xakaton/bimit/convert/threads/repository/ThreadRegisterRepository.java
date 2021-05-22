package ru.xakaton.bimit.convert.threads.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.xakaton.bimit.convert.threads.model.ThreadRegister;


@Repository
public interface ThreadRegisterRepository extends CrudRepository<ThreadRegister, UUID> {

	Optional<ThreadRegister> findByThreaduuid(UUID thread);

}
