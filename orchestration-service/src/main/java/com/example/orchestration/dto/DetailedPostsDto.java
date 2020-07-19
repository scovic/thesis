package com.example.orchestration.dto;

import com.example.orchestration.dto.iamservice.UserDto;
import com.example.orchestration.dto.postservice.PostDto;

import java.util.ArrayList;
import java.util.List;

public class DetailedPostsDto {
  List<DetailedPostDto> posts;

  public DetailedPostsDto(List<DetailedPostDto> posts) {
    this.posts = posts;
  }

  public DetailedPostsDto(List<PostDto> posts, List<UserDto> users) {
    this.posts = new ArrayList<>();

    for (int i = 0; i < posts.size(); i++) {
      this.posts.add(new DetailedPostDto(
          posts.get(i),
          users.get(i)
      ));
    }
  }

  public List<DetailedPostDto> getPosts() {
    return posts;
  }

  public void setPosts(List<DetailedPostDto> posts) {
    this.posts = posts;
  }
}
