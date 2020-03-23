package com.stefan.ticketseller.service;

import com.stefan.ticketseller.dao.PurchaseDetailsDao;
import com.stefan.ticketseller.dao.TicketsDetailsDao;
import com.stefan.ticketseller.exception.CancelRequestFailedException;
import com.stefan.ticketseller.exception.NotEnoughTicketsException;
import com.stefan.ticketseller.exception.PurchaseNotCompletedException;
import com.stefan.ticketseller.exception.PurchaseNotSavedException;
import com.stefan.ticketseller.model.PurchaseDetails;
import com.stefan.ticketseller.model.TicketsDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service("service")
public class ServiceImpl implements Service {

  private final PurchaseDetailsDao purchaseDetailsDao;
  private final TicketsDetailsDao ticketsDetailsDao;

  @Autowired
  public ServiceImpl(
      @Qualifier("purchaseDetailsDao") PurchaseDetailsDao purchaseDetailsDao,
      @Qualifier("ticketsDetailsDao") TicketsDetailsDao ticketsDetailsDao
  ) {
    this.purchaseDetailsDao = purchaseDetailsDao;
    this.ticketsDetailsDao = ticketsDetailsDao;
  }

  @Override
  public List<PurchaseDetails> getUserTickets(int userId) {
    return purchaseDetailsDao.getUsersPurchase(userId);
  }

  @Override
  public boolean purchaseTicket(int userId, int quantity) throws PurchaseNotCompletedException {
    try {
      this.sellTickets(quantity);
      this.savePurchase(userId, quantity);

      return true;
    } catch (Exception ex) {
      throw new PurchaseNotCompletedException(ex.getMessage());
    }
  }

  private void sellTickets(int quantity) throws NotEnoughTicketsException {
    TicketsDetails ticket = this.ticketsDetailsDao.getAll().get(0);
    boolean isSold = ticket.sellTickets(quantity);

    if (!isSold) {
      throw new NotEnoughTicketsException();
    }

    this.ticketsDetailsDao.update(ticket.getId(), ticket);
  }

  private void savePurchase (int userId, int quantity) throws PurchaseNotSavedException {
    Date currentDate = new Date();
    List<PurchaseDetails> list = new ArrayList<>();

    for (int i = 0; i < quantity; i++) {
      list.add(new PurchaseDetails(userId, currentDate));
    }

    try {
      this.purchaseDetailsDao.save(list);
    } catch (Exception ex) {
      throw new PurchaseNotSavedException();
    }
  }

  @Override
  public void cancelPurchase(int id) throws CancelRequestFailedException {
    try {
      this.purchaseDetailsDao.get(id); // If there is no record in db with the id, it will throw an exception
      this.purchaseDetailsDao.delete(id);
    } catch (Exception ex) {
      throw new CancelRequestFailedException(ex.getMessage());
    }
  }
}
