package com.stefan.iam.service;

import com.stefan.iam.dao.Dao;
import com.stefan.iam.exception.NotSavedException;
import com.stefan.iam.exception.UserAlreadyExistsException;
import com.stefan.iam.exception.UserNotFoundException;
import com.stefan.iam.exception.WrongCredentialsException;
import com.stefan.iam.model.User;
import com.stefan.iam.dto.UserDto;
import com.stefan.iam.util.JwtTokenUtil;
import com.stefan.iam.util.PasswordHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service("userService")
public class ServiceImpl implements Service {

  private final Dao<User> userDao;
  private final PasswordHashUtil hashUtil;
  private final JwtTokenUtil jwtUtil;

  @Autowired
  public ServiceImpl(
      @Qualifier("userDao") Dao userDao,
      PasswordHashUtil hashUtil,
      JwtTokenUtil jwtUtil
  ) {
    this.userDao = userDao;
    this.hashUtil = hashUtil;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public boolean authenticate(String jwtToken, String email) {
    return jwtUtil.validateToken(jwtToken, email);
  }

  @Override
  public List<UserDto> getUsers() {
    return this.convertToUserDto(this.userDao.getAll());
  }

  private List<UserDto> convertToUserDto (@NotNull List<User> users) {
    List<UserDto> dtos = new ArrayList<>();

    for (int i = 0; i < users.size(); i++) {
      dtos.add(UserDto.modelToDto(users.get(i)));
    }

    return dtos;
  }

  @Override
  public UserDto getUser(int id) throws UserNotFoundException {
    try {
      return UserDto.modelToDto(userDao.get(id));
    } catch (EmptyResultDataAccessException ex) {
      throw new UserNotFoundException(id);
    } catch (Exception ex) {
      throw ex;
    }

  }

  @Override
  public UserDto getUser(String email) throws UserNotFoundException {
    try {
      return UserDto.modelToDto(this.userDao.get(email));
    } catch (EmptyResultDataAccessException ex) {
      throw new UserNotFoundException(email);
    } catch (Exception ex) {
      throw ex;
    }

  }

  @Override
  public boolean deleteUser(int id) {
    return this.userDao.delete(id);
  }

  @Override
  public boolean updateUser(UserDto user) {
    return this.userDao.update(UserDto.dtoToModel(user));
  }

  @Override
  public UserDto registerUser(UserDto user) throws UserAlreadyExistsException, NotSavedException {
    if (userExists(user.getEmail())) {
      throw new UserAlreadyExistsException();
    }

    User u = UserDto.dtoToModel(user);

    u.setSalt(hashUtil.generateSalt(512).get());
    u.setPassword(hashUtil.hashPassword(user.getPassword(), u.getSalt()).get());

    try {
      User savedUser = this.userDao.save(u);
      return UserDto.modelToDto(savedUser);
    } catch (NotSavedException ex) {
      throw ex;
    }
  }

  private boolean userExists(String email) {
    try {
      userDao.get(email);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  @Override
  public String login(String email, String password) throws WrongCredentialsException {
    User user = userDao.get(email);

    if (user == null || !hashUtil.verifyPassword(password, user.getPassword(), user.getSalt())) {
      throw new WrongCredentialsException();
    }

    return jwtUtil.generateToken(email);
  }

//  @Override
//  public boolean logout(String email) {
//    return false;
//  }
}
