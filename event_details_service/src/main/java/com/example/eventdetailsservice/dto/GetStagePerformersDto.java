package com.example.eventdetailsservice.dto;

public class GetStagePerformersDto {
  private int stageId;

  public GetStagePerformersDto(int stageId) {
    this.stageId = stageId;
  }

  public int getStageId() {
    return stageId;
  }
}
