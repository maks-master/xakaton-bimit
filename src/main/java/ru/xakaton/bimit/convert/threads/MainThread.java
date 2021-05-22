package ru.xakaton.bimit.convert.threads;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import ru.xakaton.bimit.convert.threads.model.ThreadRegister;
import ru.xakaton.bimit.convert.threads.repository.ThreadRegisterRepository;

@Service
@Scope("prototype")
public class MainThread extends LongActionThread {

	public Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public static boolean processThreadStart = false;
	public static ProcessThread processThread;

	@Autowired
	private ApplicationContext applicationContext;

	//@Autowired
	private TaskExecutor longActionTaskExecutor = new SimpleAsyncTaskExecutor();

	@Autowired
	ThreadRegisterRepository threadRegisterRepository;

	public void initProcess() throws Exception {
		Calendar rightNow = Calendar.getInstance();
		rightNow.add(Calendar.SECOND, -60);
		Set<ThreadRegister> forDel = new HashSet<ThreadRegister>();
		boolean myExist = false;

		for (ThreadRegister threadRegister : threadRegisterRepository.findAll()) {
			if (threadRegister.getThreaduuid().equals(THREAD_UUID)) {
				myExist = true;

				threadRegister.setLifetime(new Timestamp((new Date()).getTime()));
				threadRegisterRepository.save(threadRegister);
			} else {
				if (threadRegister.getLifetime().before(new Timestamp(rightNow.getTimeInMillis()))) {
					forDel.add(threadRegister);
				}
			}
		}

		if (!myExist) {
			mainThreadRegister = threadRegisterRepository
					.save(new ThreadRegister(new Timestamp((new Date()).getTime()), 0, THREAD_UUID));

			if (!processThreadStart) {
				processThread = applicationContext.getBean(ProcessThread.class, mainThreadRegister);
				longActionTaskExecutor.execute(processThread);
				processThreadStart = true;
			}
		}

		threadRegisterRepository.deleteAll(forDel);

	}

	public void interrupt() {
		Thread.currentThread().isInterrupted();

		if (processThreadStart) {
			processThread.interrupt();
		}
	}
}
