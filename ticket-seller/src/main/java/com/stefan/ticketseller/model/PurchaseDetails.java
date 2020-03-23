package com.stefan.ticketseller.model;

import java.util.Date;

public class PurchaseDetails {
  private int id;
  private int userId;
  private Date purchaseDate;

  public PurchaseDetails() {}

  public PurchaseDetails(int userId, Date purchaseDate) {
    this.userId = userId;
    this.purchaseDate = purchaseDate;
  }

  public PurchaseDetails(int id, int userId, Date purchaseDate) {
    this.id = id;
    this.userId = userId;
    this.purchaseDate = purchaseDate;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Date getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(Date purchaseDate) {
    this.purchaseDate = purchaseDate;
  }
}
