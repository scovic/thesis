package com.example.orchestration.saga;

import com.example.orchestration.saga.eventdetailsservice.GetEventObjectsSaga;
import com.example.orchestration.saga.eventdetailsservice.GetStagePerformersSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDetailsServiceSagaManager {
  @Autowired
  private GetEventObjectsSaga getEventObjectsSaga;

  @Autowired
  private GetStagePerformersSaga getStagePerformersSaga;

  public GetEventObjectsSaga getGetEventObjectsSaga() {
    return getEventObjectsSaga;
  }

  public GetStagePerformersSaga getGetStagePerformersSaga() {
    return getStagePerformersSaga;
  }
}
