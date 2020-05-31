package com.stefan.ticketseller;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSubscriber {
  @Autowired
  private Connection natsConneciton;

  public void subscribe(String topic, MessageHandler messageHandler) {
    Dispatcher d = natsConneciton.createDispatcher(messageHandler);
    d.subscribe(topic);
  }
}
