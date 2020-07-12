package com.stefan.postservice.dto;

public class RawFileDto {
  private byte[] fileBytes;
  private String fileName;

  public RawFileDto(byte[] fileBytes, String fileName) {
    this.fileBytes = fileBytes;
    this.fileName = fileName;
  }

  public byte[] getFileBytes() {
    return fileBytes;
  }

  public void setFileBytes(byte[] fileBytes) {
    this.fileBytes = fileBytes;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
