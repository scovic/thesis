package com.stefan.ticketseller.dao;

import com.stefan.ticketseller.mapper.TicketsDetailsMapper;
import com.stefan.ticketseller.model.TicketsDetails;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository("ticketsDetailsDao")
public class TicketsDetailsDaoImpl implements TicketsDetailsDao {
  private final JdbcTemplate jdbcTemplate;

  public TicketsDetailsDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<TicketsDetails> getAll() {
    String sql = "SELECT * FROM \"tickets\";";

    return jdbcTemplate.query(sql, new TicketsDetailsMapper());
  }

  @Override
  public TicketsDetails get(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM \"tickets\" WHERE id = ?;";

    return this.jdbcTemplate.queryForObject(sql, new Object[]{ id }, new TicketsDetailsMapper());
  }

  @Override
  public boolean update(int id, TicketsDetails ticketsDetails) {
    String sql = "UPDATE \"tickets\" SET quantity = ?, event_name = ?, price = ? "
        + "WHERE id = ?";

    Object[] params = new Object[] {
        ticketsDetails.getQuantity(),
        ticketsDetails.getEventName(),
        ticketsDetails.getPrice(),
        id
    };

    int[] types = new int[] {
        Types.INTEGER,
        Types.VARCHAR,
        Types.INTEGER,
        Types.INTEGER
    };

    int result = jdbcTemplate.update(sql, params, types);

    return result > 0;
  }
}
