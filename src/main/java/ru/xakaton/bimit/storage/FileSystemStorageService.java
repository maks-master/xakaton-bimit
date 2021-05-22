package ru.xakaton.bimit.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path uploadLocation;
	private final Path resLocation;
	private final Path tempLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.uploadLocation = Paths.get(properties.getLocation());
		this.resLocation = Paths.get(properties.getResLocation());
		this.tempLocation = Paths.get(properties.getTempLocation());

		isDerictory(properties.getLocation());
		isDerictory(properties.getResLocation());
		isDerictory(properties.getTempLocation());
	}

	@Override
	public void store(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.uploadLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public String storeAndRename(MultipartFile file) {
		String extension = FilenameUtils.getExtension(StringUtils.cleanPath(file.getOriginalFilename()));
		String filename = UUID.randomUUID().toString() + "." + extension;
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.uploadLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
		return filename;
	}

	@Override
	public Path load(String filename) {
		return resLocation.resolve(filename);
	}

	@Override
	public void store(String to, InputStream stream) {
		try {
			Files.copy(stream, this.resLocation.resolve(to), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new StorageException("Failed to save stream file " + to, e);
		}

	}

	@Override
	public String move(String from, String to, Boolean fromUpload, Boolean saveExtension) {
		try {

			if (saveExtension)
				to += "." + FilenameUtils.getExtension(from);

			Path from_path = this.resLocation.resolve(from);
			if (fromUpload)
				from_path = this.uploadLocation.resolve(from);
			Files.move(from_path, this.resLocation.resolve(to), StandardCopyOption.REPLACE_EXISTING);

			return to;
		} catch (IOException e) {
			throw new StorageException("Failed to move file " + from, e);
		}
	}

	@Override
	public String copy(String from, String to, Boolean fromUpload, Boolean saveExtension) {
		try {
			if (saveExtension)
				to += "." + FilenameUtils.getExtension(from);

			Path from_path = this.resLocation.resolve(from);
			if (fromUpload)
				from_path = this.uploadLocation.resolve(from);
			Files.copy(from_path, this.resLocation.resolve(to), StandardCopyOption.REPLACE_EXISTING);

			return to;
		} catch (IOException e) {
			throw new StorageException("Failed to copy file " + from, e);
		}
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void delete(String filename) {
		try {
			FileSystemUtils.deleteRecursively(uploadLocation.resolve(filename));
		} catch (IOException e) {
			throw new StorageFileNotFoundException("Could not delete file: " + filename, e);
		}
	}

	@Override
	public void deleteRes(String filename) {
		try {
			FileSystemUtils.deleteRecursively(resLocation.resolve(filename));
		} catch (IOException e) {
			throw new StorageFileNotFoundException("Could not delete file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(uploadLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(uploadLocation);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	public void isDerictory(String path) {
		try {
			if (path != null && !path.isEmpty()) {
				File file = new File(path);
				if (!file.exists())
					file.mkdirs();
			}
		} catch (Exception e) {

		}
	}

	@Override
	public Path getTempLocation() {
		return tempLocation;
	}
}
