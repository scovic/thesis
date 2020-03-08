package com.stefan.iam.service;

import com.stefan.iam.dto.UserDto;
import com.stefan.iam.exception.NotSavedException;
import com.stefan.iam.exception.UserAlreadyExistsException;
import com.stefan.iam.exception.UserNotFoundException;
import com.stefan.iam.exception.WrongCredentialsException;

import java.util.List;

public interface Service {
  UserDto registerUser(UserDto user) throws UserAlreadyExistsException, NotSavedException;
  List<UserDto> getUsers();
  UserDto getUser(int id) throws UserNotFoundException;
  UserDto getUser(String email) throws UserNotFoundException;
  boolean deleteUser(int id);
  boolean updateUser(UserDto user);
  boolean authenticate(String jwtToken, String email);
  String login(String email, String password) throws WrongCredentialsException;
//  boolean logout(String email);

}
