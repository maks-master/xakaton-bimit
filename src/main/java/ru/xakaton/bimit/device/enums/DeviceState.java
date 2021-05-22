package ru.xakaton.bimit.device.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeviceState {
	ONLINE(0, "В сети"),
	OFFLINE(1, "Не в сети"),
	SERVICE(2, "Сервисный режим");

	private final String title;
	private final int value;

	DeviceState(int value, String title) {
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
	static DeviceState findValue(@JsonProperty("value") String value) {
		try {
			int x = Integer.parseInt(value);
			return Arrays.stream(DeviceState.values()).filter(v -> v.getValue() == x).findFirst().get();
		} catch (Exception s) {
			return DeviceState.valueOf(value);
		}
	}

}
