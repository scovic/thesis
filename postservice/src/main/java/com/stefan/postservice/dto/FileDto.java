package com.stefan.postservice.dto;

public class FileDto {
  private int postId;
  private String fileName;

  public FileDto(int postId, String fileName) {
    this.postId = postId;
    this.fileName = fileName;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
