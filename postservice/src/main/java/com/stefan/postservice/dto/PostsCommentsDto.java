package com.stefan.postservice.dto;

import java.util.List;

public class PostsCommentsDto {
  private List<CommentDto> comments;
  private int postId;

  public PostsCommentsDto(List<CommentDto> comments, int postId) {
    this.comments = comments;
    this.postId = postId;
  }

  public List<CommentDto> getComments() {
    return comments;
  }

  public void setComments(List<CommentDto> comments) {
    this.comments = comments;
  }

  public int getPostId() {
    return postId;
  }

  public void setPostId(int postId) {
    this.postId = postId;
  }
}
