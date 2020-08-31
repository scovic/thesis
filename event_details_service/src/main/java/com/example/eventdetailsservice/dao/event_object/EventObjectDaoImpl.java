package com.example.eventdetailsservice.dao.event_object;

import com.example.eventdetailsservice.mapper.EventObjectMapper;
import com.example.eventdetailsservice.model.EventObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventObjectDaoImpl implements EventObjectDao {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<EventObject> getEventObjects() {
    final String SQL = "SELECT * FROM \"event_object\";";
    return jdbcTemplate.query(SQL, new EventObjectMapper());
  }
}
