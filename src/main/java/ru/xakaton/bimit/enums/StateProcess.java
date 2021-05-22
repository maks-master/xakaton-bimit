package ru.xakaton.bimit.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StateProcess {

	IDLE(0, "Ожидание обработки"), RUNNING(1, "Обработка"), FINISHED_SUCCESS(2, "Обработка закончена успешно"),
	FINISHED_ERROR(3, "Обработка закончена с ошибкой"), PAUSED(4, "Обработка остановлена"),
	FINISHED_UNKNOWN(5, "Обработка остановлена роботом");

	private final String title;
	private final int value;

	StateProcess(int value, String title) {
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
	static StateProcess findValue(@JsonProperty("value") String value) {
		try {
			int x = Integer.parseInt(value);
			return Arrays.stream(StateProcess.values()).filter(v -> v.getValue() == x).findFirst().get();
		} catch (Exception s) {
			return StateProcess.valueOf(value);
		}
	}
}
