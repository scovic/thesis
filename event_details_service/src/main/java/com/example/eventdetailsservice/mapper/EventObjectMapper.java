package com.example.eventdetailsservice.mapper;

import com.example.eventdetailsservice.model.EventObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventObjectMapper implements RowMapper<EventObject> {
  @Override
  public EventObject mapRow(ResultSet resultSet, int i) throws SQLException {
    return new EventObject(
        resultSet.getInt("id"),
        resultSet.getString("name"),
        resultSet.getString("type"),
        resultSet.getDouble("latitude"),
        resultSet.getDouble("longitude")
    );
  }
}
