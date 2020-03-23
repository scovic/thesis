package com.stefan.ticketseller.exception;

public class PurchaseNotCompletedException extends Exception {
  public PurchaseNotCompletedException(String message) {
    super(message);
  }
}
