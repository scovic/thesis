package com.example.notificationsservice.model;

import com.example.notificationsservice.dto.NotificationDto;

public class Notification {
  private String title;
  private String body;
  private Data data;

  public Notification(NotificationDto notificationDto) {
    this.title = notificationDto.getTitle();
    this.body = notificationDto.getBody();
    this.data = new Data(notificationDto.getData());
  }

  public Notification(String title, String body, Data data) {
    this.title = title;
    this.body = body;
    this.data = data;
  }

  public Notification(String title, String body) {
    this.title = title;
    this.body = body;
  }

  public boolean hasData() {
    return this.data.hasValues();
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public String getTitle() {
    return title;
  }

  public String getBody() {
    return body;
  }
}
