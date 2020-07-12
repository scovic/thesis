package com.example.orchestration.dto.iamservice;

public class AuthorizeDto {
  private String token;

  public AuthorizeDto() { }

  public AuthorizeDto(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
