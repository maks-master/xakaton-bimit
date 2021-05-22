package ru.xakaton.bimit.convert.threads.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ThreadQueue {
	public static final int INIT_ACTION = 1;
	public static final int CALC_ACTION = 2;
	public static final int END_ACTION = 3;

	@Id
	@GeneratedValue
	private UUID uuid;
	
	private UUID modelUuid;

	private Timestamp lifetime;
	private int stop;// Флаг для остановки обработки

	private int sort;

	private int action;

	@JsonIgnore
	@ManyToOne
	private ThreadRegister thread;

	public ThreadQueue() {
		super();
	}

	public ThreadQueue(int sort, UUID modelUuid) {
		super();
		this.sort = sort;
		this.lifetime = new Timestamp(new Date().getTime());
		this.modelUuid = modelUuid;
		this.action = INIT_ACTION;
	}

	public ThreadQueue(int sort, ThreadRegister thread, UUID modelUuid) {
		super();
		this.sort = sort;
		this.thread = thread;
		this.modelUuid = modelUuid;
		this.lifetime = new Timestamp(new Date().getTime());
		this.action = INIT_ACTION;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Timestamp getLifetime() {
		return lifetime;
	}

	public void setLifetime(Timestamp lifetime) {
		this.lifetime = lifetime;
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public ThreadRegister getThread() {
		return thread;
	}

	public void setThread(ThreadRegister thread) {
		this.thread = thread;
	}

	public boolean isStoped() {
		return this.stop == 1;
	}

	public UUID getModelUuid() {
		return modelUuid;
	}

	public void setModelUuid(UUID modelUuid) {
		this.modelUuid = modelUuid;
	}
	
}
