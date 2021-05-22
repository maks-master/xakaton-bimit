package ru.xakaton.bimit.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

	@Value("${uploadLocation}")
	private String location;

	@Value("${resLocation}")
	private String resLocation;

	@Value("${tempLocation}")
	private String tempLocation;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getResLocation() {
		return resLocation;
	}

	public void setResLocation(String location) {
		this.resLocation = location;
	}

	public String getTempLocation() {
		return tempLocation;
	}

	public void setTempLocation(String tempLocation) {
		this.tempLocation = tempLocation;
	}

}
