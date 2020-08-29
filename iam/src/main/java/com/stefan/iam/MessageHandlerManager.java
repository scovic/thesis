package com.stefan.iam;

import com.stefan.iam.messagehandler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerManager {
  private final String topic;

  @Autowired
  public MessageHandlerManager(
      MessageSubscriber messageSubscriber,
      CreateUserMessageHandler createUserMessageHandler,
      LoginMessageHandler loginMessageHandler,
      AuthorizeMessageHandler authorizeMessageHandler,
      DeleteUserMessageHandler deleteUserMessageHandler,
      GetUsersMessageHandler getUsersMessageHandler,
      UpdateUserMessageHandler updateUserMessageHandler
  ) {
    this.topic = "iam-service-";

    messageSubscriber.subscribe(this.getTopic("create"), createUserMessageHandler);
    messageSubscriber.subscribe(this.getTopic("login"), loginMessageHandler);
    messageSubscriber.subscribe(this.getTopic("authorize"), authorizeMessageHandler);
    messageSubscriber.subscribe(this.getTopic("delete"), deleteUserMessageHandler);
    messageSubscriber.subscribe(this.getTopic("get-users"), getUsersMessageHandler);
    messageSubscriber.subscribe(this.getTopic("update-user"), updateUserMessageHandler);
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.topic, t);
  }
}
