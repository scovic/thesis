package com.stefan.postservice.service;

import com.stefan.postservice.dao.CommentDao;
import com.stefan.postservice.dao.PostDao;
import com.stefan.postservice.exception.FailedServiceOperationException;
import com.stefan.postservice.model.Comment;
import com.stefan.postservice.model.Post;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

  @Autowired
  private PostDao postDao;

  @Autowired
  private CommentDao commentDao;

  @Override
  public List<Post> getAll() {
    return this.postDao.getAll();
  }

  @Override
  public Post getPost(int postId) {
    return this.postDao.get(postId);
  }

  @Override
  public Post addPost(Post post) throws FailedServiceOperationException {
    try {
      return this.postDao.save(post);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }

  @Override
  public void deletePost(int postId) throws FailedServiceOperationException {
    try {
      this.postDao.delete(postId);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }

  @Override
  public void updatePost(Post post) throws FailedServiceOperationException {
    try {
      post.update();
      this.postDao.update(post);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }

  @Override
  public Comment addComment(Comment comment) throws FailedServiceOperationException {
    try {
      return this.commentDao.save(comment);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }

  @Override
  public List<Comment> getPostsComments(int postId) throws FailedServiceOperationException {
    try {
      return this.commentDao.getPostsComment(postId);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }

  @Override
  public List<Post> getAuthorsPosts(int authorId) throws FailedServiceOperationException {
    try {
      return this.postDao.getAuthorsPosts(authorId);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }

  @Override
  public void deleteAuthorsPostsAndComments(int userId) throws FailedServiceOperationException {
    try {
      this.postDao.deleteAuthorsPosts(userId);
      this.commentDao.deleteAuthorsComments(userId);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }

  @Override
  public void deleteComment(int commentId) throws FailedServiceOperationException {
    try {
      this.commentDao.delete(commentId);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }

  }

  @Override
  public void updateComment(Comment comment) throws FailedServiceOperationException {
    try {
      comment.update();
      this.commentDao.update(comment);
    } catch (Exception ex) {
      throw new FailedServiceOperationException(ex.getMessage());
    }
  }
}
