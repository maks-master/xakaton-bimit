package ru.xakaton.bimit.device.model;

import java.sql.Timestamp;
import java.util.List;

public class AlarmTimeLine {
	private List<AlarmDTO> alarms;
	private Timestamp time;
	
	public AlarmTimeLine(List<AlarmDTO> alarms, Timestamp time) {
		super();
		this.alarms = alarms;
		this.time = time;
	}
	public List<AlarmDTO> getAlarms() {
		return alarms;
	}
	public void setAlarms(List<AlarmDTO> alarms) {
		this.alarms = alarms;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	
}
