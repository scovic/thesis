package com.example.orchestration.dto;

import com.example.orchestration.dto.iamservice.UserDto;
import com.example.orchestration.dto.postservice.PostDto;

public class DetailedPostDto {
  private PostDto post;
  private UserDto author;

  public DetailedPostDto(PostDto post, UserDto author) {
    this.post = post;
    this.author = author;
  }

  public PostDto getPost() {
    return post;
  }

  public void setPost(PostDto post) {
    this.post = post;
  }

  public UserDto getAuthor() {
    return author;
  }

  public void setAuthor(UserDto author) {
    this.author = author;
  }
}
