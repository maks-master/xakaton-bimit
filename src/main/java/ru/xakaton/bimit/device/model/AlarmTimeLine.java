package ru.xakaton.bimit.device.model;

import java.util.List;

public class AlarmTimeLine {
	private List<AlarmDTO> alarms;
	private Long time;
	
	public AlarmTimeLine(List<AlarmDTO> alarms, Long time) {
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
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}

	
	
}
