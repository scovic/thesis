package com.stefan.postservice.dao;

import com.stefan.postservice.exception.FailedDbOperationException;
import com.stefan.postservice.mapper.CommentMapper;
import com.stefan.postservice.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Date;
import java.util.List;

@Repository
public class CommentDaoImpl implements CommentDao {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Comment> getPostsComment(int postId) {
    final String sql = "SELECT * FROM \"comments\" WHERE post_id = ?;";
    return jdbcTemplate.query(sql, new Object[]{postId}, new CommentMapper());
  }

  @Override
  public Comment get(int id) {
    final String sql = "SELECT * FROM \"comments\" WHERE id = ?;";
    return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CommentMapper());
  }

  @Override
  public Comment save(Comment comment) throws FailedDbOperationException {
    final String sql = "INSERT INTO \"comments\" (text, author_id, post_id)"
        + "VALUES (?, ?, ?);";

    KeyHolder holder = new GeneratedKeyHolder();

    jdbcTemplate.update((Connection connection) -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      ps.setString(1, comment.getText());
      ps.setInt(2, comment.getAuthorId());
      ps.setInt(3, comment.getPostId());

      return ps;
    }, holder);

    comment.setId((int) holder.getKeys().get("id"));
    comment.setCreatedAt((Date) holder.getKeys().get("created_at"));
    comment.setUpdatedAt((Date) holder.getKeys().get("updated_at"));

    return comment;
  }

  @Override
  public void delete(int id) throws FailedDbOperationException {
    String sql = "DELETE FROM \"comments\" WHERE id = ?;";

    int result = jdbcTemplate.update(sql, id);

    if (result != 1) {
      throw new FailedDbOperationException("delete post");
    }
  }

  @Override
  public void deleteAuthorsComments(int authorId) throws FailedDbOperationException {
    final String sql = "DELETE FROM \"comments\" WHERE author_id = ?;";
    jdbcTemplate.update(sql, authorId);
  }

  @Override
  public void update(Comment comment) throws FailedDbOperationException {
    String sql = "UPDATE \"comments\"" +
        "SET text = ?, updated_at = ? " +
        "WHERE id = ?;";

    final Object[] params = new Object[]{
        comment.getText(),
        new Timestamp(comment.getUpdatedAt().getTime()),
        comment.getId()
    };

    final int[] types = {
        Types.VARCHAR,
        Types.TIMESTAMP,
        Types.INTEGER
    };

    int result = jdbcTemplate.update(sql, params, types);

    if (result != 1) {
      throw new FailedDbOperationException("update post");
    }
  }
}
