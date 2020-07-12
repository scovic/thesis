package com.example.orchestration.dto.postservice;

import java.util.Date;

public class CommentDto {
  private int id;
  private String text;
  private int authorId;
  private int postId;
  private Date createdAt;
  private Date updatedAt;

  public CommentDto() {
  }

  public CommentDto(int id, String text, int authorId, int postId) {
    this.id = id;
    this.text = text;
    this.authorId = authorId;
    this.postId = postId;
  }

  public CommentDto(int id, String text, int authorId, int postId, Date createdAt, Date updatedAt) {
    this.id = id;
    this.text = text;
    this.authorId = authorId;
    this.postId = postId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getAuthorId() {
    return authorId;
  }

  public void setAuthorId(int authorId) {
    this.authorId = authorId;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
