package ru.xakaton.bimit.device.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeviceType {
	TEMPERATURE(0, "Температура"),
	LUMINOSITY(1, "Освещение"),
	POWER(2, "Электроэнергия");

	private final String title;
	private final int value;

	DeviceType(int value, String title) {
		this.title = title;
		this.value = value;
	}

	public String getName() {
		return name();
	}

	public String getTitle() {
		return title;
	}

	public int getValue() {
		return value;
	}

	@JsonCreator
	static DeviceType findValue(@JsonProperty("value") String value) {
		try {
			int x = Integer.parseInt(value);
			return Arrays.stream(DeviceType.values()).filter(v -> v.getValue() == x).findFirst().get();
		} catch (Exception s) {
			return DeviceType.valueOf(value);
		}
	}

}
