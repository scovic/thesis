package com.stefan.iam.dao;

import com.stefan.iam.exception.NotSavedException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface Dao<T> {
  List<T> getAll();
  T get(int id) throws EmptyResultDataAccessException;
  T get(String email) throws EmptyResultDataAccessException;
  T save(T t) throws NotSavedException;
  boolean delete(int id);
  boolean update(T t);
}
