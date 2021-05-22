package ru.xakaton.bimit.convert;

import ru.xakaton.bimit.enums.StateProcess;
import ru.xakaton.bimit.model.Model;
import ru.xakaton.bimit.model.ProcessState;

public abstract class LongAction implements Runnable {

	private ProcessState processState;

	public ProcessState getProcessState() {
		return processState;
	}

	public void setProcessState(ProcessState processState) {
		this.processState = processState;
	}

	public abstract void createProcess(Model model);

	public abstract void updateProcess(StateProcess stateProcess, double progress);

	public abstract void updateProcess(StateProcess stateProcess);

	public abstract void updateProcess(StateProcess stateProcess, String info);

	public abstract void unRegisterProcess();

	public abstract void registerProcess();

}
