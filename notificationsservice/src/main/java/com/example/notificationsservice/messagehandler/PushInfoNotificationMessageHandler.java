package com.example.notificationsservice.messagehandler;

import com.example.notificationsservice.MessageHandler;
import com.example.notificationsservice.dto.NotificationDto;
import com.example.notificationsservice.message.CommandMessage;
import com.example.notificationsservice.message.ReplyMessage;
import com.example.notificationsservice.model.Notification;
import com.example.notificationsservice.service.Service;
import com.example.notificationsservice.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class PushInfoNotificationMessageHandler extends MessageHandler<NotificationDto, Boolean> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<NotificationDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<NotificationDto>>() {
    }.getType();

    return (CommandMessage<NotificationDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(NotificationDto notificationDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      this.service.publishInfoNotification(new Notification(notificationDto));
      replyMessage.setData(true);
    } catch (Exception ex) {
      replyMessage.setFailure();
    }

    return replyMessage;
  }
}
