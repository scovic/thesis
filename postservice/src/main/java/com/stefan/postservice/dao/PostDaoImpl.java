package com.stefan.postservice.dao;

import com.stefan.postservice.exception.FailedDbOperationException;
import com.stefan.postservice.mapper.PostMapper;
import com.stefan.postservice.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Date;
import java.util.List;

@Repository
public class PostDaoImpl implements PostDao {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Post> getAll() {
    final String sql = "SELECT * FROM \"posts\";";
    return jdbcTemplate.query(sql, new PostMapper());
  }

  @Override
  public List<Post> getAuthorsPosts(int authorId) {
    final String sql = "SELECT * FROM \"posts\" WHERE author_id = ?;";
    return jdbcTemplate.query(sql, new Object[] {authorId}, new PostMapper());
  }

  @Override
  public Post get(int id) {
    final String sql = "SELECT * FROM \"posts\" WHERE id = ?;";
    return jdbcTemplate.queryForObject(sql, new Object[]{id}, new PostMapper());
  }

  @Override
  public Post save(Post post) throws FailedDbOperationException {
    final String sql = "INSERT INTO \"posts\" (text, author_id)"
        + "VALUES (?, ?);";

    KeyHolder holder = new GeneratedKeyHolder();

    jdbcTemplate.update((Connection connection) -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      ps.setString(1, post.getText());
      ps.setInt(2, post.getAuthorId());

      return ps;
    }, holder);

    post.setId((int) holder.getKeys().get("id"));
    post.setCreatedAt((Date) holder.getKeys().get("created_at"));
    post.setUpdatedAt((Date) holder.getKeys().get("updated_at"));
    return post;
  }

  @Override
  public void delete(int id) throws FailedDbOperationException {
    String sql = "DELETE FROM \"posts\" WHERE id = ?;";

    int result = jdbcTemplate.update(sql, id);

    if (result != 1) {
      throw new FailedDbOperationException("delete post");
    }
  }

  @Override
  public void deleteAuthorsPosts(int authorsId) throws FailedDbOperationException {
    final String sql = "DELETE FROM \"posts\" WHERE author_id = ?;";
    jdbcTemplate.update(sql, authorsId);
  }

  @Override
  public void update(Post post) throws FailedDbOperationException {
    String sql = "UPDATE \"posts\"" +
        "SET text = ?, updated_at = ? " +
        "WHERE id = ?;";

    final Object[] params = new Object[]{
        post.getText(),
        new Timestamp(post.getUpdatedAt().getTime()),
        post.getId()
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
