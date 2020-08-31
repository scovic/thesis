package com.example.eventdetailsservice.mapper;

import com.example.eventdetailsservice.model.Performer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PerformerMapper implements RowMapper<Performer> {
  @Override
  public Performer mapRow(ResultSet resultSet, int i) throws SQLException {
    return new Performer(
        resultSet.getInt("id"),
        resultSet.getInt("stage_id"),
        resultSet.getString("name"),
        resultSet.getTimestamp("start_time")
    );
  }
}
