package com.stefan.postservice.dto;

import java.util.List;

public class DetailedPostDto {
    private PostDto postDto;
    private List<CommentDto> comments;

  public DetailedPostDto(PostDto postDto, List<CommentDto> comments) {
    this.postDto = postDto;
    this.comments = comments;
  }

  public PostDto getPostDto() {
    return postDto;
  }

  public void setPostDto(PostDto postDto) {
    this.postDto = postDto;
  }

  public List<CommentDto> getComments() {
    return comments;
  }

  public void setComments(List<CommentDto> comments) {
    this.comments = comments;
  }
}
