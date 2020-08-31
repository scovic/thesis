package com.example.eventdetailsservice.dto;

public class PerformerDto {
  private int id;
  private int stageId;
  private String name;
  private long startTime;

  public PerformerDto(int id, int stageId, String name, long startTime) {
    this.id = id;
    this.stageId = stageId;
    this.name = name;
    this.startTime = startTime;
  }
}
