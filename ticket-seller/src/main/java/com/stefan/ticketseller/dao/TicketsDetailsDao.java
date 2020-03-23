package com.stefan.ticketseller.dao;

import com.stefan.ticketseller.model.TicketsDetails;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface TicketsDetailsDao {
  List<TicketsDetails> getAll();
  TicketsDetails get(int id) throws EmptyResultDataAccessException;
  boolean update(int id, TicketsDetails ticketsDetails);
}
