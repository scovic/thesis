package com.example.eventdetailsservice.model;

import com.example.eventdetailsservice.dto.PerformerDto;

import java.util.Date;

public class Performer {
  private int id;
  private int stageId;
  private String name;
  private Date startTime;

  public Performer(int id, int stageId, String name, Date startTime) {
    this.id = id;
    this.stageId = stageId;
    this.name = name;
    this.startTime = startTime;
  }

  public PerformerDto getData() {
    return new PerformerDto(
        id,
        stageId,
        name,
        startTime.getTime()
    );
  }
}
