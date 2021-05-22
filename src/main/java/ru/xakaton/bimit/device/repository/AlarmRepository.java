package ru.xakaton.bimit.device.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import ru.xakaton.bimit.device.model.Alarm;

public interface AlarmRepository extends CrudRepository<Alarm, UUID>{

	List<Alarm> findAllByDeviceUuidAndTimeAfterOrderByTime(UUID uuid, Timestamp time);
	List<Alarm> findAllByTimeAfterOrderByTime(Timestamp time);
}
