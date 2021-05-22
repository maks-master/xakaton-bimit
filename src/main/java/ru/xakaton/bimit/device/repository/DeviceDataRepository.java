package ru.xakaton.bimit.device.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import ru.xakaton.bimit.device.model.DeviceData;

public interface DeviceDataRepository extends CrudRepository<DeviceData, UUID>{

}
