package com.stefan.ticketseller.mapper;

import com.stefan.ticketseller.model.PurchaseDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseDetailsMapper implements RowMapper<PurchaseDetails> {
  @Override
  public PurchaseDetails mapRow(ResultSet resultSet, int i) throws SQLException {
    PurchaseDetails purchaseDetails = new PurchaseDetails();

    purchaseDetails.setId(resultSet.getInt("id"));
    purchaseDetails.setUserId(resultSet.getInt("user_id"));
    purchaseDetails.setPurchaseDate(resultSet.getDate("purchase_date"));

    return purchaseDetails;
  }
}
