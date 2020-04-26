package com.stefan.iam;

import io.nats.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {
  @Autowired
  private Connection natsConnection;

  public void publish(String topic, String message) {
    natsConnection.publish(topic, message.getBytes());
  }
}
