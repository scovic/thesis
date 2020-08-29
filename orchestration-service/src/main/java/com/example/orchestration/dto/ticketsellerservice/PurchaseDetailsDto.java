package com.example.orchestration.dto.ticketsellerservice;

import org.joda.time.DateTime;

import java.util.Date;

public class PurchaseDetailsDto {
  private int id;
  private int userId;
  private long purchaseDate;

  public PurchaseDetailsDto(int id, int userId, Date purchaseDate) {
    this.id = id;
    this.userId = userId;
    this.purchaseDate = purchaseDate.getTime();
  }

  public PurchaseDetailsDto(int id, int userId, long purchaseDateTimestamp) {
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

  public long getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(long purchaseDate) {
    this.purchaseDate = purchaseDate;
  }
}
