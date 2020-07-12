package com.stefan.postservice.mapper;

import com.stefan.postservice.model.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostMapper implements RowMapper<Post> {
  @Override
  public Post mapRow(ResultSet resultSet, int i) throws SQLException {
    return new Post(
        resultSet.getInt("id"),
        resultSet.getString("text"),
        resultSet.getInt("author_id"),
        resultSet.getTimestamp("created_at"),
        resultSet.getTimestamp("updated_at")
    );
  }
}
