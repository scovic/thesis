package com.example.eventdetailsservice.dao.performer;

import com.example.eventdetailsservice.model.Performer;

import java.util.List;

public interface PerformerDao {
  List<Performer> getStagePerformers(int stageId);
}
