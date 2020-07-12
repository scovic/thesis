package com.stefan.postservice.model;

import com.stefan.postservice.dto.PostDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {
  private int id;
  private String text;
  private int authorId;
  private Date createdAt;
  private Date updatedAt;
  private List<String> attachmentNames;

  public Post() {
    createdAt = new Date();
    updatedAt = new Date();
    attachmentNames = new ArrayList<>();
  }

  public Post(PostDto postDto) {
    this.id = postDto.getId();
    this.text = postDto.getText();
    this.authorId = postDto.getAuthorId();
    this.createdAt = new Date();
    this.updatedAt = new Date();
    attachmentNames = new ArrayList<>();
  }

  public Post(String text, int authorId) {
    this.id = -1;
    this.authorId = authorId;
    this.text = text;
    createdAt = new Date();
    updatedAt = new Date();
    attachmentNames = new ArrayList<>();
  }

  public Post(int id, String text, int authorId) {
    this.id = id;
    this.text = text;
    this.authorId = authorId;
    createdAt = new Date();
    updatedAt = new Date();
    attachmentNames = new ArrayList<>();
  }

  public Post(int id, String text, int authorId, Date createdAt, Date updatedAt) {
    this.id = id;
    this.text = text;
    this.authorId = authorId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    attachmentNames = new ArrayList<>();
  }

  public static List<PostDto> convertPostListToPostDtoList(List<Post> posts) {
    List<PostDto> list = new ArrayList<>();

    for(Post post : posts) {
      list.add(post.getPostData());
    }

    return list;
  }

  public void update() {
    this.updatedAt = new Date();
  }

  public PostDto getPostData() {
    return new PostDto(
        this.id,
        this.text,
        this.getAuthorId(),
        this.getCreatedAt(),
        this.getUpdatedAt()
    );
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

  public void setAttachmentNames(List<String> attachmentNames) {
    this.attachmentNames = attachmentNames;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
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

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public List<String> getAttachmentNames() {
    return attachmentNames;
  }


}
