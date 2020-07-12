package com.stefan.iam.dto;

public class AuthorizeDto {
  private String token;

  public AuthorizeDto(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String jwtToken) {
    this.token = jwtToken;
  }
}
