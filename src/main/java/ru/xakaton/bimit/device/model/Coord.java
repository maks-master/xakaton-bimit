package ru.xakaton.bimit.device.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Coord {
	
	@Id
	@GeneratedValue
	private UUID uuid;

	private Double x;
	private Double y;
	private Double z;
	
	public Double getX() {
		return x;
	}
	
	public void setX(Double x) {
		this.x = x;
	}
	
	public Double getY() {
		return y;
	}
	
	public void setY(Double y) {
		this.y = y;
	}
	
	public Double getZ() {
		return z;
	}
	
	public void setZ(Double z) {
		this.z = z;
	}

}
