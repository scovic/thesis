package com.stefan.postservice.exception;

public class FailedDbOperationException extends Exception {
    public FailedDbOperationException() {
      super("Db operation failed!");
    }
    public FailedDbOperationException(String operation) {
      super(String.format("Db operation [%s] failed", operation));
    }
}
