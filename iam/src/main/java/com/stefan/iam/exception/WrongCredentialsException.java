package com.stefan.iam.exception;

public class WrongCredentialsException extends Exception {
  public WrongCredentialsException() {
    super("Incorrect email/password");
  }
}
