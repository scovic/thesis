package com.stefan.postservice.model;

import com.stefan.postservice.dto.CommentDto;

import java.util.Date;

public class Comment {
  private int id;
  private String text;
  private int authorId;
  private int postId;
  private Date createdAt;
  private Date updatedAt;

  public Comment(CommentDto commentDto) {
    this.id = commentDto.getId();
    this.text = commentDto.getText();
    this.authorId = commentDto.getAuthorId();
    this.postId = commentDto.getPostId();
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  public Comment(int id, String text, int authorId, int postId, Date createdAt, Date updatedAt) {
    this.id = id;
    this.text = text;
    this.authorId = authorId;
    this.postId = postId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Comment(int id, String text, int authorId, int postId) {
    this.id = id;
    this.text = text;
    this.authorId = authorId;
    this.postId = postId;
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  public Comment(String text, int authorId, int postId) {
    this.id = -1;
    this.text = text;
    this.authorId = authorId;
    this.postId = postId;
    this.createdAt = new Date();
    this.updatedAt = new Date();
  }

  public CommentDto getCommentData() {
    return new CommentDto(
        this.id,
        this.text,
        this.authorId,
        this.postId,
        this.createdAt,
        this.updatedAt
    );
  }

  public void update() {
    this.updatedAt = new Date();
  }

  public int getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public int getAuthorId() {
    return authorId;
  }

  public int getPostId() {
    return postId;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
