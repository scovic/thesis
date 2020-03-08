package com.stefan.iam.exception;

public class UserNotFoundException extends Exception {
  public UserNotFoundException(int id) {
    super(String.format("User with id: %d does not exists", id));
  }

  public UserNotFoundException(String email) {
    super(String.format("User with email: %s does not exists.", email));
  }
}
