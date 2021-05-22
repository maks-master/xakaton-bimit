package ru.xakaton.bimit.device.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AlarmLevel {
	WARNING(0, "Внимание"),
	ERROR(1, "Ошибка"),
	STRONG(2, "Важно"),
	CRITICAL(3, "Критичный уровень");

	private final String title;
	private final int value;

	AlarmLevel(int value, String title) {
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
	static AlarmLevel findValue(@JsonProperty("value") String value) {
		try {
			int x = Integer.parseInt(value);
			return Arrays.stream(AlarmLevel.values()).filter(v -> v.getValue() == x).findFirst().get();
		} catch (Exception s) {
			return AlarmLevel.valueOf(value);
		}
	}

}
