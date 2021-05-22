package ru.xakaton.bimit.device.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import ru.xakaton.bimit.device.model.DeviceState;


public interface DeviceStateRepository extends CrudRepository<DeviceState, UUID>{

 	Optional<DeviceState> findFirstByDeviceUuid(UUID deviceUuid);
}
