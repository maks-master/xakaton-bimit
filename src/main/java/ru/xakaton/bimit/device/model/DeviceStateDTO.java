package ru.xakaton.bimit.device.model;

import java.util.UUID;

public class DeviceStateDTO {
	
	private UUID deviceUuid;
	private UUID alarmUuid;
	
	private java.sql.Timestamp time;
	
	private Double min;
	private Double max;
	private Double average;
	private Double mediana;
	
	private String color;

	public UUID getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(UUID deviceUuid) {
		this.deviceUuid = deviceUuid;
	}


	public UUID getAlarmUuid() {
		return alarmUuid;
	}

	public void setAlarmUuid(UUID alarmUuid) {
		this.alarmUuid = alarmUuid;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
