package com.example.eventdetailsservice.model;

import com.example.eventdetailsservice.dto.EventObjectDto;

public class EventObject {
  private final String STAGE_TYPE = "STAGE";
  private final String EXIT_TYPE = "EXIT";
  private final String WC_TYPE = "WC";

  private int id;
  private String name;
  private String type;
  private double latitude;
  private double longitude;

  public EventObject(int id, String name, String type, double latitude, double longitude) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public EventObjectDto getData() {
    return new EventObjectDto(
        id,
        name,
        type,
        latitude,
        longitude
    );
  }
}
