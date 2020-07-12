package com.stefan.postservice.service;

import com.stefan.postservice.exception.FailedServiceOperationException;
import com.stefan.postservice.model.Comment;
import com.stefan.postservice.model.Post;

import java.util.List;

public interface Service {
  List<Post> getAll();
  Post getPost(int postId);
  Post addPost(Post post) throws FailedServiceOperationException;
  void deletePost(int postId) throws FailedServiceOperationException;
  void updatePost(Post post) throws FailedServiceOperationException;

  List<Comment> getPostsComments(int postId) throws FailedServiceOperationException;
  List<Post> getAuthorsPosts(int authorId) throws FailedServiceOperationException;
  void deleteAuthorsPostsAndComments(int authorId) throws FailedServiceOperationException;

  Comment addComment(Comment comment) throws FailedServiceOperationException;
  void deleteComment(int commentId) throws FailedServiceOperationException;
  void updateComment(Comment comment) throws FailedServiceOperationException;


}
