package ru.xakaton.bimit.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.xakaton.bimit.storage.StorageProperties;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@EntityScan(basePackages = { "ru.xakaton" })
@EnableJpaRepositories(basePackages = { "ru.xakaton" })
@EnableTransactionManagement
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class BimitConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	
}