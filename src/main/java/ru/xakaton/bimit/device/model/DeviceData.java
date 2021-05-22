package ru.xakaton.bimit.device.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class DeviceData {
	@Id
	@GeneratedValue
	private UUID uuid;
	
	private UUID deviceUuid;
	
	private Double data;
	private int count;
	
	@DateTimeFormat()
	private java.sql.Timestamp time;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(UUID deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public java.sql.Timestamp getTime() {
		return time;
	}

	public void setTime(java.sql.Timestamp time) {
		this.time = time;
	}
	
	
}
