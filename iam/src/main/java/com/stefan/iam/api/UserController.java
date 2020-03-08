package com.stefan.iam.api;

import com.stefan.iam.dto.UserDto;
import com.stefan.iam.exception.UserAlreadyExistsException;
import com.stefan.iam.exception.UserNotFoundException;
import com.stefan.iam.service.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/iam/users")
@RestController
public class UserController {
  private final Service service;

  @Autowired
  public UserController(@Qualifier("userService") Service service) {
    this.service = service;
  }

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @ResponseBody
  public ResponseEntity<UserDto> register(UserDto user) {
    try {
      return new ResponseEntity<>(this.service.registerUser(user), HttpStatus.CREATED);
    } catch (UserAlreadyExistsException ex) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          String.format(ex.getMessage()),
          ex
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          String.format("Something went wrong: %s", ex.getMessage()),
          ex
      );
    }
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<UserDto>> getAllUsers() {
    return new ResponseEntity<>(this.service.getUsers(), HttpStatus.OK);
  }

  @GetMapping(path = "{id}")
  @ResponseBody
  public ResponseEntity<UserDto> getUser(@PathVariable("id") int id) {
    try {
      return new ResponseEntity<>(service.getUser(id), HttpStatus.OK);
    } catch (UserNotFoundException ex) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          ex.getMessage(),
          ex
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Something went wrong",
          ex
      );
    }
  }

  @GetMapping(params = { "email" })
  @ResponseBody
  public ResponseEntity<UserDto> getUser(@RequestParam() String email) {
    try {
      return new ResponseEntity<>(service.getUser(email), HttpStatus.OK);
    } catch (UserNotFoundException ex) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          ex.getMessage(),
          ex
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Something went wrong",
          ex
      );
    }
  }

  @DeleteMapping(path = "{id}")
  @ResponseBody
  public ResponseEntity<Boolean> deleteUserById(@PathVariable("id") int id) {
    return new ResponseEntity<>(service.deleteUser(id), HttpStatus.OK);
  }

  @PutMapping(path = "{id}")
  @ResponseBody
  public ResponseEntity<Boolean> updateUser(@PathVariable("id") int id, @RequestBody UserDto user) {
    user.setId(id);
    return new ResponseEntity<>(service.updateUser(user), HttpStatus.OK);
  }
}
