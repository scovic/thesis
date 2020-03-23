package com.stefan.ticketseller.exception;

public class PurchaseNotSavedException extends Exception {
  public PurchaseNotSavedException() {
    super("Something went wrong while trying to save purchase details to db");
  }
}
