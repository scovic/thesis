package com.example.eventdetailsservice.dto;

import java.util.List;

public class EventObjectsDto {
  List<EventObjectDto> eventObjects;

  public EventObjectsDto(List<EventObjectDto> eventObjects) {
    this.eventObjects = eventObjects;
  }
}
