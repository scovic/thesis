package com.stefan.iam.dao;

import com.stefan.iam.exception.NotSavedException;
import com.stefan.iam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

@Repository("userDao")
public class UserDao implements Dao<User> {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public UserDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<User> getAll() {
    final String sql = "SELECT * FROM \"users\";";

    return this.jdbcTemplate.query(sql, (resultSet, i) -> {
      User user = new User();

      user.setId(resultSet.getInt("id"));
      user.setEmail(resultSet.getString("email"));
      user.setFirstName(resultSet.getString("firstName"));
      user.setLastName(resultSet.getString("lastName"));
//      user.setPassword(resultSet.getString("password"));

      return user;
    });
  }

  @Override
  @Nullable
  public User get(int id) throws EmptyResultDataAccessException {
    final String sql = "SELECT * FROM \"users\" WHERE id = ?;";

    try {
      return jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setPassword(resultSet.getString("password"));

        return user;
      });
    } catch (EmptyResultDataAccessException e) {
      throw e;
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
  @Nullable
  public User get(String email) throws EmptyResultDataAccessException {
    final String sql = "SELECT * FROM \"users\" WHERE email= ?;";

    try {
      return jdbcTemplate.queryForObject(sql, new Object[]{email}, (resultSet, i) -> {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setFirstName(resultSet.getString("firstName"));
        user.setLastName(resultSet.getString("lastName"));
        user.setPassword(resultSet.getString("password"));
        user.setSalt(resultSet.getString("salt"));

        return user;
      });
    } catch (EmptyResultDataAccessException e) {
      throw e;
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
  public User save(User user) throws NotSavedException {
    final String sql = "INSERT INTO \"users\" (email, password, salt, firstName, lastName)" +
            "VALUES (?, ?, ?, ?, ?);";

    KeyHolder holder = new GeneratedKeyHolder();

    int result = jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      ps.setString(1, user.getEmail());
      ps.setString(2, user.getPassword());
      ps.setString(3, user.getSalt());
      ps.setString(4, user.getFirstName());
      ps.setString(5, user.getLastName());

      return ps;
    }, holder);

    user.setId((int) holder.getKeys().get("id"));
    return user;
  }

  @Override
  public boolean update(User user) {
    final StringBuilder sql = new StringBuilder("UPDATE \"users\" SET ");

//    sql.append(String.format("email = '%s', ", user.getEmail()));
    sql.append(String.format("firstName = '%s', ", user.getFirstName()));
    sql.append(String.format("lastName = '%s' ", user.getLastName()));

    if (user.getPassword() != null    ) {
      sql.append(", password = '");
      sql.append(user.getPassword());
      sql.append("'");
    }

    sql.append(String.format("WHERE id = %d ;", user.getId()));

    int result = jdbcTemplate.update(sql.toString());
    return result == 1;
  }

  @Override
  public boolean delete(int id)  {
    final String sql = "DELETE FROM \"users\" WHERE id = ?;";

    int result = jdbcTemplate.update(sql, new Object[] {id}, new int[] { Types.INTEGER });

    return result == 1;
  }
}
