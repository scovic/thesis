package com.example.eventdetailsservice.dto;

import java.util.List;

public class PerformersDto {
  List<PerformerDto> performerDtoList;

  public PerformersDto(List<PerformerDto> performerDtoList) {
    this.performerDtoList = performerDtoList;
  }
}
