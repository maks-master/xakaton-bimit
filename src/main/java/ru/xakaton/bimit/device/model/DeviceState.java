package ru.xakaton.bimit.device.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class DeviceState {

	@Id
	@GeneratedValue
	private UUID uuid;
	
	private UUID deviceUuid;
	
	@DateTimeFormat()
	private java.sql.Timestamp time;
	
	private Double min;
	private Double max;
	private Double average;
	private Double mediana;
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
	public java.sql.Timestamp getTime() {
		return time;
	}
	public void setTime(java.sql.Timestamp time) {
		this.time = time;
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	public Double getMediana() {
		return mediana;
	}
	public void setMediana(Double mediana) {
		this.mediana = mediana;
	}
	
	
	
}
