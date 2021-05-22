package ru.xakaton.bimit.device.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import ru.xakaton.bimitenums.DeviceState;
import ru.xakaton.bimitenums.DeviceType;

@Entity
public class Device {

	@Id
	@GeneratedValue
	private UUID uid;
	
	private DeviceType deviceType;
	private String name;
	private int frequency;
	private Double minValue;
	private Double maxValue;
	private DeviceState deviceState;
	
	@OneToOne
	private Coord position;
	@OneToOne
	private Coord cameraPosition;
	
	public UUID getUid() {
		return uid;
	}
	
	public void setUid(UUID uid) {
		this.uid = uid;
	}
	
	public DeviceType getDeviceType() {
		return deviceType;
	}
	
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public Double getMinValue() {
		return minValue;
	}
	
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	
	public Double getMaxValue() {
		return maxValue;
	}
	
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	
	public DeviceState getDeviceState() {
		return deviceState;
	}
	
	public void setDeviceState(DeviceState deviceState) {
		this.deviceState = deviceState;
	}

}
