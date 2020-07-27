package com.example.notificationsservice.model;

import java.util.Map;

public class Data {
  private Map<String, Object> data;

  public Data(Map<String, Object> data) {
    this.data = data;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public boolean hasValues() {
    return !data.isEmpty();
  }
}
