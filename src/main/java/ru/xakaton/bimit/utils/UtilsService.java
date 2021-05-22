package ru.xakaton.bimit.utils;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class UtilsService {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(UtilsService.class);

	public static void isDirectory(String path) {
		try {
			if (path != null && !path.isEmpty()) {
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				} else
					LOGGER.info("file exists");
			} else
				LOGGER.info("path null");
		} catch (Exception e) {
			LOGGER.error("== Error test and created directory " + path + ":", e);
		}
	}
}
