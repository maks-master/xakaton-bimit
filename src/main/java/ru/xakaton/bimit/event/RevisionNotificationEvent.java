package ru.xakaton.bimit.event;

import ru.xakaton.bimit.model.Model;

public class RevisionNotificationEvent extends AbstractNotificationEvent<Model> {

	public RevisionNotificationEvent(Object source, Model eventObject, String eventAction) {
		super(source, eventObject, eventAction);
	}
}
