package com.example.orchestration.dto.ticketsellerservice;

public class GetUserTicketsReqDto {
  private int userId;

  public GetUserTicketsReqDto(int userId) {
    this.userId = userId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}
