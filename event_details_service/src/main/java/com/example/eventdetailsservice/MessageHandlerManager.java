package com.example.eventdetailsservice;

import com.example.eventdetailsservice.messagehandler.GetEventObjectsMessageHandler;
import com.example.eventdetailsservice.messagehandler.GetStagePerformersMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerManager {
  private final String topic;

  @Autowired
  public MessageHandlerManager(
      MessageSubscriber messageSubscriber,
      GetEventObjectsMessageHandler getEventObjectsMessageHandler,
      GetStagePerformersMessageHandler getStagePerformersMessageHandler
  ) {
    this.topic = "event-details-service-";

    messageSubscriber.subscribe(this.getTopic("get-event-objects"), getEventObjectsMessageHandler);
    messageSubscriber.subscribe(this.getTopic("get-stage-performers"), getStagePerformersMessageHandler);
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.topic, t);
  }
}
