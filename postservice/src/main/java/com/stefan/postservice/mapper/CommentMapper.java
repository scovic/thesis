package com.stefan.postservice.mapper;

import com.stefan.postservice.model.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper implements RowMapper<Comment> {
  @Override
  public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
    return new Comment(
        resultSet.getInt("id"),
        resultSet.getString("text"),
        resultSet.getInt("author_id"),
        resultSet.getInt("post_id"),
        resultSet.getTimestamp("created_at"),
        resultSet.getTimestamp("updated_at")
    );
  }
}
