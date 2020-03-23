package com.stefan.ticketseller.dao;

import com.stefan.ticketseller.exception.PurchaseNotSavedException;
import com.stefan.ticketseller.mapper.PurchaseDetailsMapper;
import com.stefan.ticketseller.model.PurchaseDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
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
  public void save(PurchaseDetails purchaseDetails) throws PurchaseNotSavedException {
    try {
      this.saveOneRow(purchaseDetails);
    } catch (Exception ex) {
      throw new PurchaseNotSavedException();
    }
  }

  private void saveOneRow(PurchaseDetails purchaseDetails) {
    String sql = "INSERT INTO \"purchase_details\" (user_id, purchase_date) " +
        "VALUES (?, ?);";

    Object[] params = new Object[] {
        purchaseDetails.getUserId(),
        purchaseDetails.getPurchaseDate().getTime()
    };

    int[] types = {
        Types.INTEGER,
        Types.TIMESTAMP
    };

    jdbcTemplate.update(sql, params, types);
  }

  @Override
  public void save(List<PurchaseDetails> purchaseDetails) throws PurchaseNotSavedException {
    try {
      this.saveMultipleRows(purchaseDetails);
    } catch (Exception ex) {
      throw new PurchaseNotSavedException();
    }
  }

  private void saveMultipleRows(List<PurchaseDetails> purchaseDetails) {
    String sql = "INSERT INTO \"purchase_details\" (user_id, purchase_date)" +
        "VALUES (?, ?);";

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
        PurchaseDetails pd = purchaseDetails.get(i);

        preparedStatement.setInt(1, pd.getUserId());
        preparedStatement.setTimestamp(2, new Timestamp(pd.getPurchaseDate().getTime()));
      }


      @Override
      public int getBatchSize() {
        return purchaseDetails.size();
      }
    });
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
