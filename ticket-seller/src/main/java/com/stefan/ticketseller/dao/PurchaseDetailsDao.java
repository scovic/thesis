package com.stefan.ticketseller.dao;

import com.stefan.ticketseller.exception.PurchaseNotSavedException;
import com.stefan.ticketseller.model.PurchaseDetails;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface PurchaseDetailsDao {
  List<PurchaseDetails> getAll();
  List<PurchaseDetails> getUsersPurchase(int userId);
  PurchaseDetails get(int id) throws EmptyResultDataAccessException;
  PurchaseDetails save(PurchaseDetails purchaseDetails) throws PurchaseNotSavedException;
  List<PurchaseDetails> save(List<PurchaseDetails> purchaseDetails) throws PurchaseNotSavedException;
  boolean delete(int id);
  boolean update(int id, PurchaseDetails purchaseDetails);
}
