package com.example.eventdetailsservice.dto;

public class EventObjectDto {
  private int id;
  private String name;
  private String type;
  private double latitude;
  private double longitude;

  public EventObjectDto(int id, String name, String type, double latitude, double longitude) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
