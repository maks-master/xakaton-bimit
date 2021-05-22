package ru.xakaton.bimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import ru.xakaton.bimit.convert.threads.LongActionThread;
import ru.xakaton.bimit.convert.threads.MainThread;


@Component
public class BimitEventListener implements ApplicationListener<ApplicationReadyEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

	private static LongActionThread mainThread;
	private static boolean mainThreadProcessStart = false;

	@Autowired
	private ApplicationContext applicationContext;

	private TaskExecutor longActionTaskExecutor = new SimpleAsyncTaskExecutor();

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		if (!mainThreadProcessStart) {
			mainThread = applicationContext.getBean(MainThread.class);
			longActionTaskExecutor.execute(mainThread);
			mainThreadProcessStart = true;
		}
	}

	public static LongActionThread getMainThread() {
		return mainThread;
	}

	public static void setMainThread(LongActionThread mainThread) {
		BimitEventListener.mainThread = mainThread;
	}

}
