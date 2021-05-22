package ru.xakaton.bimit.event;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractNotificationEvent<T> extends ApplicationEvent {

	private static final long serialVersionUID = 6294775244186975389L;

	private String eventAction;

	private T eventObject;

	public AbstractNotificationEvent(Object source, T eventObject, String eventAction) {
		super(source);
		setEventObject(eventObject);
		setEventAction(eventAction);
	}

	public T getEventObject() {
		return eventObject;
	}

	public void setEventObject(T eventObject) {
		this.eventObject = eventObject;
	}

	public String getEventAction() {
		return eventAction;
	}

	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}
}
