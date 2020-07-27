package com.example.notificationsservice.message;

public class CommandMessage<T> {
  private T data;

  public CommandMessage(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
