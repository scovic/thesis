package com.stefan.ticketseller.dao;

import com.stefan.ticketseller.exception.PurchaseNotSavedException;
import com.stefan.ticketseller.mapper.PurchaseDetailsMapper;
import com.stefan.ticketseller.model.PurchaseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository("purchaseDetailsDao")
public class PurchaseDetailsDaoImpl implements PurchaseDetailsDao {
  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public PurchaseDetailsDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<PurchaseDetails> getAll() {
    String sql = "SELECT * FROM \"purchase_details\";";

    return this.jdbcTemplate.query(sql, new PurchaseDetailsMapper());
  }

  @Override
  public List<PurchaseDetails> getUsersPurchase(int userId) {
    String sql = "SELECT * FROM \"purchase_details\" WHERE user_id = ?;";

    return this.jdbcTemplate.query(sql, new Object[]{ userId }, new PurchaseDetailsMapper());
  }

  @Override
  public PurchaseDetails get(int id) throws EmptyResultDataAccessException {
    String sql = "SELECT * FROM \"purchase_details\" WHERE id = ?;";

    return this.jdbcTemplate.queryForObject(sql, new Object[]{ id }, new PurchaseDetailsMapper());
  }

  @Override
  public PurchaseDetails save(PurchaseDetails purchaseDetails) throws PurchaseNotSavedException {
    try {
      return this.saveOneRow(purchaseDetails);
    } catch (Exception ex) {
      throw new PurchaseNotSavedException();
    }
  }

  @Override
  public List<PurchaseDetails> save(List<PurchaseDetails> purchaseDetails) throws PurchaseNotSavedException {
    try {
      for (PurchaseDetails pd: purchaseDetails) {
        pd.setId(this.saveOneRow(pd).getId());
      }

      return purchaseDetails;
    } catch (Exception ex) {
      throw new PurchaseNotSavedException();
    }
  }

  private PurchaseDetails saveOneRow(PurchaseDetails purchaseDetails) {
    String sql = "INSERT INTO \"purchase_details\" (user_id, purchase_date) " +
        "VALUES (?, ?);";

    KeyHolder holder = new GeneratedKeyHolder();

    int result = jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      ps.setInt(1, purchaseDetails.getUserId());
      ps.setTimestamp(2, new Timestamp(purchaseDetails.getPurchaseDate().getTime()));

      return ps;
    }, holder);

    purchaseDetails.setId((int) holder.getKeys().get("id"));
    return purchaseDetails;
  }

  @Override
  public boolean delete(int id) {
    String sql = "DELETE FROM \"purchase_details\" WHERE id = ?;";

    int result = jdbcTemplate.update(sql, new Object[] {id}, new int[] { Types.INTEGER });
    return result == 1;
  }

  @Override
  public boolean update(int id, PurchaseDetails purchaseDetails) {
    String sql = "UPDATE \"purchase_details\" SET user_id = ?, purchase_date = ? " +
        "WHERE id = ?;";

    Object[] params = new Object[] {
        purchaseDetails.getUserId(),
        purchaseDetails.getPurchaseDate().getTime(),
        id
    };

    int[] types = new int[] {
        Types.INTEGER,
        Types.TIMESTAMP,
        Types.INTEGER
    };

    int result = jdbcTemplate.update(sql, params, types);
    return result > 0;
  }
}
