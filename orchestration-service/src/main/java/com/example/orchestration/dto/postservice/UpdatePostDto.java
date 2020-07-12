package com.example.orchestration.dto.postservice;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdatePostDto {
  private PostDto post;
  private List<RawFileDto> files;

  public UpdatePostDto(PostDto post, MultipartFile[] files) throws IOException {
    this.post = post;
    this.files = new ArrayList<>();

    for (MultipartFile file : files) {
      this.files.add(new RawFileDto(
          file.getBytes(),
          file.getOriginalFilename()
      ));
    }
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
