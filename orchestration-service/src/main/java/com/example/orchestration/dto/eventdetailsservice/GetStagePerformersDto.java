package com.example.orchestration.dto.eventdetailsservice;

public class GetStagePerformersDto {
  private int stageId;

  public GetStagePerformersDto(int stageId) {
    this.stageId = stageId;
  }

  public int getStageId() {
    return stageId;
  }


}
