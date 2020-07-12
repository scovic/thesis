package com.stefan.postservice.exception;

public class FailedServiceOperationException extends Exception {
  public FailedServiceOperationException() {
    super("Service operation has failed");
  }

  public FailedServiceOperationException(String message) {
    super(String.format("Service operation failed - %s" , message));
  }
}
