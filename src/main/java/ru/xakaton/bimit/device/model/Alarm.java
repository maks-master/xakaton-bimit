package ru.xakaton.bimit.device.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import ru.xakaton.bimit.device.enums.AlarmLevel;

public class Alarm {
	@Id
	@GeneratedValue
	private UUID uuid;
	
	private AlarmLevel alarmLevel;
	
	@DateTimeFormat()
	private java.sql.Timestamp time;
	
	@Column(columnDefinition = "text")
	private String into;

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

	public java.sql.Timestamp getTime() {
		return time;
	}

	public void setTime(java.sql.Timestamp time) {
		this.time = time;
	}

	public String getInto() {
		return into;
	}

	public void setInto(String into) {
		this.into = into;
	}
	
	
	
}
