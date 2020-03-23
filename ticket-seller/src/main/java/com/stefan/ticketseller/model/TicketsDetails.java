package com.stefan.ticketseller.model;

public class TicketsDetails {
  private int id;
  private String eventName;
  private int quantity;
  private int price;

  public TicketsDetails() { }

  public TicketsDetails(String eventName, int quantity, int price) {
    this.eventName = eventName;
    this.quantity = quantity;
    this.price = price;
  }

  private boolean canBeSold(int quantity) {
    if (this.getQuantity() - quantity < 0) {
      return false;
    }

    return true;
  }

  public boolean sellTickets(int quantity) {
    if (this.canBeSold(quantity)){
      this.setQuantity(this.getQuantity() - quantity);
      return true;
    }

    return false;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }
}
