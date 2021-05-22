package ru.xakaton.bimit.model;

import java.util.UUID;

public class ModelDTO {

	private UUID uuid;
	private String title;
	
	private String file;
	private String xktFile;
	private String jsonFile;
	
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getXktFile() {
		return xktFile;
	}
	public void setXktFile(String xktFile) {
		this.xktFile = xktFile;
	}
	public String getJsonFile() {
		return jsonFile;
	}
	public void setJsonFile(String jsonFile) {
		this.jsonFile = jsonFile;
	}
	
	

}
