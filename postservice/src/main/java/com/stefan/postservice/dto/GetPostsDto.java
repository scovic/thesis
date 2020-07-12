package com.stefan.postservice.dto;

import java.util.List;

public class GetPostsDto {
  private List<PostDto> posts;

  public GetPostsDto(List<PostDto> posts) {
    this.posts = posts;
  }

  public List<PostDto> getPosts() {
    return posts;
  }

  public void setPosts(List<PostDto> posts) {
    this.posts = posts;
  }
}
