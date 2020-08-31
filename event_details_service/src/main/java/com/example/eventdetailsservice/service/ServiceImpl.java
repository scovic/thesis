package com.example.eventdetailsservice.service;

import com.example.eventdetailsservice.dao.event_object.EventObjectDao;
import com.example.eventdetailsservice.dao.performer.PerformerDao;
import com.example.eventdetailsservice.model.EventObject;
import com.example.eventdetailsservice.model.Performer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceImpl implements com.example.eventdetailsservice.service.Service {
  @Autowired
  private EventObjectDao eventObjectDao;

  @Autowired
  private PerformerDao performerDao;

  @Override
  public List<EventObject> getEventsObjects() {
    return eventObjectDao.getEventObjects();
  }

  @Override
  public List<Performer> getStagePerformers(int stageId) {
    return performerDao.getStagePerformers(stageId);
  }
}
