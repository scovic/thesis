package com.stefan.ticketseller.mapper;

import com.stefan.ticketseller.model.TicketsDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketsDetailsMapper implements RowMapper<TicketsDetails> {
  @Override
  public TicketsDetails mapRow(ResultSet resultSet, int i) throws SQLException {
    TicketsDetails ticketsDetails = new TicketsDetails();

    ticketsDetails.setId(resultSet.getInt("id"));
    ticketsDetails.setQuantity(resultSet.getInt("quantity"));
    ticketsDetails.setEventName(resultSet.getString("event_name"));
    ticketsDetails.setPrice(resultSet.getInt("price"));

    return ticketsDetails;
  }
}
