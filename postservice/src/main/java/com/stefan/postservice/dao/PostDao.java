package com.stefan.postservice.dao;

import com.stefan.postservice.exception.FailedDbOperationException;
import com.stefan.postservice.model.Post;

import java.util.List;

public interface PostDao {
  List<Post> getAll();
  Post get(int id);
  List<Post> getAuthorsPosts(int authorId);
  Post save(Post post) throws FailedDbOperationException;
  void delete(int id) throws FailedDbOperationException;
  void deleteAuthorsPosts(int authorsId) throws FailedDbOperationException;
  void update(Post t) throws FailedDbOperationException;
}
