package com.stefan.postservice.api;


import com.stefan.postservice.dto.CommentDto;
import com.stefan.postservice.dto.DetailedPostDto;
import com.stefan.postservice.dto.PostDto;
import com.stefan.postservice.model.Comment;
import com.stefan.postservice.model.Post;
import com.stefan.postservice.service.FileStorageService;
import com.stefan.postservice.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class Api {
  @Autowired
  private Service service;

  @Autowired
  private FileStorageService fileStorageService;

  @GetMapping
  public ResponseEntity<?> getPosts() {
    try {
      List<PostDto> posts = Post.convertPostListToPostDtoList(service.getAll());

      for (PostDto post : posts) {
        post.setAttachmentNames(
            this.fileStorageService.listDirFileNames(String.valueOf(post.getId()))
        );
      }

      return new ResponseEntity<List<PostDto>>(
          Post.convertPostListToPostDtoList(service.getAll()),
          HttpStatus.OK
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ex.getMessage(), ex
      );
    }

  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getPost(@PathVariable("id") int postId) {
    try {
      List<CommentDto> comments = new ArrayList<>();
      for (Comment comment : this.service.getPostsComments(postId)) {
        comments.add(comment.getCommentData());
      }

      PostDto post = this.service.getPost(postId).getPostData();

      post.setAttachmentNames(
          this.fileStorageService.listDirFileNames(String.valueOf(post.getId()))
      );

      return new ResponseEntity<DetailedPostDto>(
          new DetailedPostDto(
              post,
              comments
          ),
          HttpStatus.OK
      );

    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ex.getMessage(), ex
      );
    }
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> addPost(@RequestParam("files") MultipartFile[] files, PostDto post) {
    try {
      PostDto createdPost = service.addPost(new Post(post)).getPostData();

      if (files.length > 0) {
        List<String> attachmentNames = new ArrayList<>();

        for (MultipartFile file : files) {
          attachmentNames.add(file.getOriginalFilename());
          this.fileStorageService.storeFile(
              file.getBytes(),
              file.getOriginalFilename(),
              String.valueOf(createdPost.getId())
          );
        }

        createdPost.setAttachmentNames(attachmentNames);
      }

      return new ResponseEntity<PostDto>(
          createdPost,
          HttpStatus.CREATED
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ex.getMessage(), ex
      );
    }
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<?> deletePost(@PathVariable("id") int postId) {
    try {
      service.deletePost(postId);
      this.fileStorageService.removeFile(Integer.toString(postId));

      return new ResponseEntity<Boolean>(
          true,
          HttpStatus.OK
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ex.getMessage(), ex
      );
    }
  }

  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updatePost(@RequestParam("files") MultipartFile[] files, PostDto post) {
    try {
      this.service.updatePost(new Post(post));

      for (MultipartFile file : files) {
        this.fileStorageService.storeFile(
            file.getBytes(),
            file.getOriginalFilename(),
            String.valueOf(post.getId())
        );
      }

      return new ResponseEntity<Boolean>(
          true,
          HttpStatus.OK
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ex.getMessage(), ex
      );
    }
  }

  @PostMapping(path = "/comments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<?> addComment(CommentDto comment) {
    try {
      return new ResponseEntity<>(
          service.addComment(new Comment(comment)).getCommentData(),
          HttpStatus.OK
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ex.getMessage(), ex
      );
    }
  }

  @DeleteMapping(path = "/comments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<?> deleteComment(CommentDto comment) {
    try {
      this.service.deleteComment(comment.getId());

      return new ResponseEntity<Boolean>(
          true,
          HttpStatus.OK
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ex.getMessage(), ex
      );
    }
  }

  @PutMapping(path = "/comments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<?> updateComment(CommentDto comment) {
    try {
      this.service.updateComment(new Comment(comment));

      return new ResponseEntity<Boolean>(
          true,
          HttpStatus.OK
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ex.getMessage(), ex
      );
    }
  }
}
