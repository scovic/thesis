package com.stefan.postservice.service;

import com.stefan.postservice.config.FileStorageConfig;
import com.stefan.postservice.exception.FailedServiceOperationException;
import com.stefan.postservice.exception.StorageNotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {
  private final Path fileStorageLocation;

  @Autowired
  public FileStorageServiceImpl(FileStorageConfig fileStorageConfig) throws StorageNotCreatedException {
    this.fileStorageLocation = Paths.get(fileStorageConfig.getStoragePath())
        .toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new StorageNotCreatedException();
    }
  }

  private String getPath(String relativePath) {
    return Paths.get(String.format("%s/%s", this.fileStorageLocation.toString(), relativePath))
        .normalize()
        .toString();
  }

  @Override
  public File getFile(String fileName, String directory) throws FailedServiceOperationException {
    File file = new File(this.getPath(String.format("%s/%s", directory, fileName)));

    if (!file.exists()) {
      throw new FailedServiceOperationException("getFile - File " + fileName + "does not exists!");
    }

    return file;
  }

  @Override
  public byte[] getBytesFromFile(String fileName, String directory) throws FailedServiceOperationException {
    Path path = Paths.get(this.getPath(String.format("%s/%s", directory, fileName)));

    try {
      return Files.readAllBytes(path);
    } catch (IOException ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }

  @Override
  public void removeFile(String filePath) throws FailedServiceOperationException {
    File file = new File(this.getPath(filePath));

    if (!file.exists()) {
      return;
    }

    if (!file.isDirectory()) {
      if (!file.delete()) {
        throw new FailedServiceOperationException("removeFile - " + filePath + " - couldn't be deleted");
      }

      return;
    }

    for (File dirItem : file.listFiles()) {
      dirItem.delete();
    }

    if (!file.delete()) {
      throw new FailedServiceOperationException("removeFile - " + filePath + " - couldn't be deleted");
    }
  }

  @Override
  public List<String> listDirFileNames(String directoryName) throws FailedServiceOperationException {
    File directory = new File(this.getPath(directoryName));

    if (!directory.isDirectory()) {
      return new ArrayList<>();
    }

    File[] files = directory.listFiles();

    if (files == null) {
      return Collections.emptyList();
    }

    return Stream.of(files)
        .filter(file -> !file.isDirectory())
        .map(File::getName)
        .collect(Collectors.toList());
  }

  @Override
  public void storeFile(byte[] fileData, String fileName, String directory) throws FileNotFoundException, IOException {
    Path fileDirPath = Paths.get(String.format("%s/%s", fileStorageLocation.toString(), directory));

    if (!Files.exists(fileDirPath)) {
      Files.createDirectories(fileDirPath);
    }

    Path filePath = Paths.get(String.format("%s/%s", fileDirPath.toString(), fileName));

    if (!Files.exists(filePath)) {
      Files.createFile(filePath);
    }

    File newFile = new File(filePath.toString());
    OutputStream out =  new FileOutputStream(newFile);
    out.write(fileData);
    out.close();
  }
}
