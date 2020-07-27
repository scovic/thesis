package com.example.notificationsservice;

import com.example.notificationsservice.messagehandler.PushInfoNotificationMessageHandler;
import com.example.notificationsservice.messagehandler.PushWarningNotificationMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerManager {
  private final String topic;

  @Autowired
  public MessageHandlerManager(
      MessageSubscriber messageSubscriber,
      PushInfoNotificationMessageHandler pushInfoNotificationMessageHandler,
      PushWarningNotificationMessageHandler pushWarningNotificationMessageHandler
  ) {
    this.topic = "notifications-service-";

    messageSubscriber.subscribe(this.getTopic("warning"), pushWarningNotificationMessageHandler);
    messageSubscriber.subscribe(this.getTopic("info"), pushInfoNotificationMessageHandler);
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.topic, t);
  }
}
