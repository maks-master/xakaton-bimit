package ru.xakaton.bimit.utils;

import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
public class EntityId {
	
	@Id
	@GeneratedValue(generator="IdOrGenerated")
	@GenericGenerator(name="IdOrGenerated", strategy="ru.xakaton.bimit.utils.UseIdOrGenerate")
	private UUID uuid;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
