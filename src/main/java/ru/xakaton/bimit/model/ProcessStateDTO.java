package ru.xakaton.bimit.model;

import java.util.UUID;

import ru.xakaton.bimit.enums.StateProcess;


public class ProcessStateDTO {

	private UUID uuid;
	private String name;

	private ModelDTO model;

	private double progress;

	private java.sql.Timestamp startDate;
	private java.sql.Timestamp endDate;

	private StateProcess stateProcess;

	private String info;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ModelDTO getModel() {
		return model;
	}

	public void setModel(ModelDTO model) {
		this.model = model;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public java.sql.Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(java.sql.Timestamp startDate) {
		this.startDate = startDate;
	}

	public java.sql.Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(java.sql.Timestamp endDate) {
		this.endDate = endDate;
	}

	public StateProcess getStateProcess() {
		return stateProcess;
	}

	public void setStateProcess(StateProcess stateProcess) {
		this.stateProcess = stateProcess;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	

}
