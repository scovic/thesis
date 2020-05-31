package com.example.orchestration.dto.ticketsellerservice;

public class PurchaseTicketsReqDto {
  private int userId;
  private int quantity;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
