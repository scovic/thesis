package com.example.orchestration.dto.ticketsellerservice;

import org.joda.time.DateTime;

import java.util.Date;

public class PurchaseDetailsDto {
  private int id;
  private int userId;
  private Date purchaseDate;

  public PurchaseDetailsDto(int id, int userId, Date purchaseDate) {
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