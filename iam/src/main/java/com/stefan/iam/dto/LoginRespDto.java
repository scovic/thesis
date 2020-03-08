package com.stefan.iam.dto;

public class LoginRespDto {
  private String token;

  public LoginRespDto(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String jwtToken) {
    this.token = jwtToken;
  }
}
