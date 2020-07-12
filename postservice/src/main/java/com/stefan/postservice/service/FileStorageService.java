package com.stefan.postservice.service;

import com.stefan.postservice.exception.FailedServiceOperationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileStorageService {
  File getFile(String fileName, String directory) throws FailedServiceOperationException;
  byte[] getBytesFromFile(String fileName, String directory) throws FailedServiceOperationException;
  void removeFile(String filePath) throws FailedServiceOperationException;
  List<String> listDirFileNames(String directoryName) throws FailedServiceOperationException;

  void storeFile(byte[] fileData, String fileName, String director) throws FileNotFoundException, IOException;
}
