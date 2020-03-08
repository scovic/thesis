package com.stefan.iam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stefan.iam.model.User;

public class UserDto {
  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  public static UserDto modelToDto(User user) {
    UserDto dto = new UserDto();

    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());

    return dto;
  }

  public static User dtoToModel(UserDto dto) {
    User user = new User();

    user.setEmail(dto.getEmail());
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());

    if (dto.getPassword() != null) {
      user.setPassword(dto.getPassword());
    }

    if (dto.getId() != 0) {
      user.setId(dto.getId());
    }

    return user;
  }

  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  @JsonProperty("id")
  public int getId() {
    return id;
  }

  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  @JsonProperty("password")
  public void setPassword(String password) {
    this.password = password;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
  }

  @JsonProperty("id")
  public void setId(int id) {
    this.id = id;
  }

  @JsonProperty("firstName")
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @JsonProperty("lastName")
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
