package ru.xakaton.bimit.convert.threads.model;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ThreadRegister {

	@Id
	@GeneratedValue
	private UUID uuid;

	private Timestamp lifetime;
	private int main;
	private UUID threaduuid;

	@OneToMany(mappedBy = "thread", cascade = CascadeType.ALL)
	private Set<ThreadQueue> queues;

	public ThreadRegister() {
		super();
	}

	public ThreadRegister(Timestamp lifetime, int main, UUID threaduuid) {
		super();
		this.lifetime = lifetime;
		this.main = main;
		this.threaduuid = threaduuid;
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

	public int getMain() {
		return main;
	}

	public void setMain(int main) {
		this.main = main;
	}

	public UUID getThreaduuid() {
		return threaduuid;
	}

	public void setThreaduuid(UUID threaduuid) {
		this.threaduuid = threaduuid;
	}

	public Set<ThreadQueue> getQueues() {
		return queues;
	}

	public void setQueues(Set<ThreadQueue> queues) {
		this.queues = queues;
	}

	public boolean isMain() {
		return this.main == 1;
	}

}
