package ru.xakaton.bimit.device.model;

import java.util.UUID;

import ru.xakaton.bimit.device.enums.AlarmLevel;

public class AlarmDTO {
	private UUID uuid;
	private UUID deviceUuid;
	private UUID deviceDataUuid;
	private AlarmLevel alarmLevel;
	private java.sql.Timestamp startTime;
	private java.sql.Timestamp endTime;
	private String info;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public AlarmLevel getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(AlarmLevel alarmLevel) {
		this.alarmLevel = alarmLevel;
	}



	public java.sql.Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(java.sql.Timestamp startTime) {
		this.startTime = startTime;
	}

	public java.sql.Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(java.sql.Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public UUID getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(UUID deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public UUID getDeviceDataUuid() {
		return deviceDataUuid;
	}

	public void setDeviceDataUuid(UUID deviceDataUuid) {
		this.deviceDataUuid = deviceDataUuid;
	}
	
	
	
}
