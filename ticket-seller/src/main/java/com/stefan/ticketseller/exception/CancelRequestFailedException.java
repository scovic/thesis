package com.stefan.ticketseller.exception;

public class CancelRequestFailedException extends Exception {
  public CancelRequestFailedException(String reason) {
    super(String.format("Cancel request failed: %s", reason));
  }

}
