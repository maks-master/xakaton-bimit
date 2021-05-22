package ru.xakaton.bimit.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.xakaton.bimit.enums.StateProcess;

@Entity
public class ProcessState {

	@Id
	@GeneratedValue
	private UUID uuid;
	private String name;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Model model;

	private double progress;

	@DateTimeFormat()
	private java.sql.Timestamp startDate;
	@DateTimeFormat()
	private java.sql.Timestamp endDate;

	private StateProcess stateProcess;

	@Column(columnDefinition = "text")
	private String info;

	public ProcessState(StateProcess idle) {
		this.stateProcess = idle;
	}

	public ProcessState() {
		super();
	}

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

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
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
