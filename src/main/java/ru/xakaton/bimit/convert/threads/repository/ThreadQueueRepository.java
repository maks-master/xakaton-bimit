package ru.xakaton.bimit.convert.threads.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.xakaton.bimit.convert.threads.model.ThreadQueue;

@Repository
public interface ThreadQueueRepository extends CrudRepository<ThreadQueue, UUID> {

	@Query("select max(sort) from ThreadQueue")
	Integer maxSortValue();

	Optional<ThreadQueue> findByModelUuid(UUID uuid);
}
