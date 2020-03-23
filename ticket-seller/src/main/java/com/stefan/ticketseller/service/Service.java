package com.stefan.ticketseller.service;

import com.stefan.ticketseller.exception.CancelRequestFailedException;
import com.stefan.ticketseller.exception.PurchaseNotCompletedException;
import com.stefan.ticketseller.model.PurchaseDetails;

import java.util.List;

public interface Service {
  List<PurchaseDetails> getUserTickets(int userId);
  boolean purchaseTicket(int userId, int quantity) throws PurchaseNotCompletedException;
  void cancelPurchase(int id) throws CancelRequestFailedException;
}
