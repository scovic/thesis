package com.example.orchestration.dto.postservice;

import java.util.List;

public class PostsDto {
  private List<PostDto> posts;

  public PostsDto(List<PostDto> posts) {
    this.posts = posts;
  }

  public List<PostDto> getPosts() {
    return posts;
  }

  public void setPosts(List<PostDto> posts) {
    this.posts = posts;
  }
}
