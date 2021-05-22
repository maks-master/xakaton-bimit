package ru.xakaton.bimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class XakatonBimitWebApplication extends SpringBootServletInitializer {

	// for war
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(XakatonBimitWebApplication.class);
	}

	// for jar
	public static void main(String[] args) {
		SpringApplication.run(XakatonBimitWebApplication.class, args);
	}
}
