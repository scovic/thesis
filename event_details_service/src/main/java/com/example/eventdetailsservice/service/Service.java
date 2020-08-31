package com.example.eventdetailsservice.service;

import com.example.eventdetailsservice.model.EventObject;
import com.example.eventdetailsservice.model.Performer;

import java.util.List;

public interface Service {
  List<EventObject> getEventsObjects();

  List<Performer> getStagePerformers(int stageId);
}
