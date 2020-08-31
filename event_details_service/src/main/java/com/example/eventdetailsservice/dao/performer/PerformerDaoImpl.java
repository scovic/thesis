package com.example.eventdetailsservice.dao.performer;

import com.example.eventdetailsservice.mapper.PerformerMapper;
import com.example.eventdetailsservice.model.Performer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PerformerDaoImpl implements PerformerDao {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public List<Performer> getStagePerformers(int stageId) {
    final String SQL = "SELECT * FROM \"performer\" WHERE stage_id = ? ORDER BY \"start_time\" ASC;";
    return jdbcTemplate.query(SQL, new Object[]{stageId}, new PerformerMapper());
  }
}
