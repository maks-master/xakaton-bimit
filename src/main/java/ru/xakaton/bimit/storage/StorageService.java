package ru.xakaton.bimit.storage;

import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void init();

	void store(String to, InputStream stream);

	void store(MultipartFile file);

	String storeAndRename(MultipartFile file);

	Path load(String filename);

	String move(String from, String to, Boolean fromUpload, Boolean saveExtension);

	String copy(String from, String to, Boolean fromUpload, Boolean saveExtension);

	Resource loadAsResource(String filename);

	void delete(String filename);

	void deleteRes(String filename);

	void deleteAll();

	Path getTempLocation();

}
