package com.stefan.iam.exception;

public class UserAlreadyExistsException extends Exception {
  public UserAlreadyExistsException() {
    super("User with provided email already exists.");
  }
}
