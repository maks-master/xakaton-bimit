package ru.xakaton.bimit.convert;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class StreamGobbler implements Runnable {

	private InputStream errorStream;
	private InputStream inputStream;
	private Consumer<String> consumer;

	public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
		this.inputStream = inputStream;
		this.consumer = consumer;
	}

	public StreamGobbler(InputStream inputStream, InputStream errorStream, Consumer<String> consumer) {
		this.inputStream = inputStream;
		this.errorStream = errorStream;
		this.consumer = consumer;
	}

	@Override
	public void run() {
		new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);

		new BufferedReader(new InputStreamReader(errorStream)).lines().forEach(consumer);
	}

}
