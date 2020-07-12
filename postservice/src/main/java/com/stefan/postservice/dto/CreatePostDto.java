package com.stefan.postservice.dto;

import java.util.List;

public class CreatePostDto {
  private PostDto post;
  private List<RawFileDto> files;

  public CreatePostDto(PostDto post, List<RawFileDto> files) {
    this.post = post;
    this.files = files;
  }

  public PostDto getPost() {
    return post;
  }

  public void setPost(PostDto post) {
    this.post = post;
  }

  public List<RawFileDto> getFiles() {
    return files;
  }

  public void setFiles(List<RawFileDto> files) {
    this.files = files;
  }
}
