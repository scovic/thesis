package com.stefan.iam.dto;

public class AuthorizeDto {
  private String token;
  private String email;

  public AuthorizeDto(String token, String email) {
    this.token = token;
    this.email = email;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String jwtToken) {
    this.token = jwtToken;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
