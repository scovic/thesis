package com.stefan.ticketseller.dto;

import java.util.Date;

public class PurchaseTicketsRespDto {
  private int id;
  private int userId;
  private Date purchaseDate;

  public PurchaseTicketsRespDto() { }

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
