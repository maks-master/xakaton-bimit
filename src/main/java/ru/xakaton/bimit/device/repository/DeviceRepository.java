package ru.xakaton.bimit.device.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import ru.xakaton.bimit.device.enums.DeviceType;
import ru.xakaton.bimit.device.model.Device;

public interface DeviceRepository extends CrudRepository<Device, UUID>{

	List<Device> findAllByDeviceType(DeviceType type);
	
}
