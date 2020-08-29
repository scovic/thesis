package com.example.orchestration.dto.postservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDto {
  int id;
  private String text;
  private int authorId;
  private double latitude;
  private double longitude;
  private long createdAt;
  private long updatedAt;
  private List<String> attachmentNames;

  public PostDto() {
    attachmentNames = new ArrayList<>();
  }

  public PostDto(int id, String text, int authorId, double lat, double lon) {
    this.id = id;
    this.text = text;
    this.authorId = authorId;
    this.latitude = lat;
    this.longitude = lon;
  }

  public PostDto(String text, int authorId) {
    this.id = -1;
    this.text = text;
    this.authorId = authorId;
  }

  public PostDto(int id, String text, int authorId, long createdAt, long updatedAt, double lat, double lon) {
    this.id = id;
    this.text = text;
    this.authorId = authorId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.latitude = lat;
    this.longitude = lon;
  }

  public int getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public int getAuthorId() {
    return authorId;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setAuthorId(int authorId) {
    this.authorId = authorId;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(long updatedAt) {
    this.updatedAt = updatedAt;
  }

  public List<String> getAttachmentNames() {
    return attachmentNames;
  }

  public void setAttachmentNames(List<String> attachmentNames) {
    this.attachmentNames = attachmentNames;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
