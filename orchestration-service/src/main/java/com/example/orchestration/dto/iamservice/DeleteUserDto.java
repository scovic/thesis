package com.example.orchestration.dto.iamservice;

public class DeleteUserDto {
  private int id;

  public DeleteUserDto() { }

  public DeleteUserDto(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
