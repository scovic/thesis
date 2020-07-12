package com.stefan.postservice.dao;

import com.stefan.postservice.exception.FailedDbOperationException;
import com.stefan.postservice.model.Comment;

import java.util.List;

public interface CommentDao {
  List<Comment> getPostsComment(int postId);
  Comment get(int id);
  Comment save(Comment comment) throws FailedDbOperationException;
  void delete(int id) throws FailedDbOperationException;
  void deleteAuthorsComments(int authorId) throws FailedDbOperationException;
  void update(Comment t) throws FailedDbOperationException;
}
