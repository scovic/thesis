package com.example.orchestration.proxy;

import com.example.orchestration.MessagePublisher;
import com.example.orchestration.dto.notificationservice.NotificationDto;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.util.GenericTypeUtil;
import com.example.orchestration.util.JsonUtil;
import io.nats.client.Message;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class NotificationServiceProxy {
  private String notificationServiceProxy;
  private MessagePublisher messagePublisher;

  @Autowired
  public NotificationServiceProxy(MessagePublisher messagePublisher) {
    this.notificationServiceProxy = "notifications-service-";
    this.messagePublisher = messagePublisher;
  }

  public Observable<ReplyMessage<Boolean>> makeInfoNotification(CommandMessage<NotificationDto> msg) {
    return this.messagePublisher.request(this.getTopic("info"), JsonUtil.toJson(msg))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  public Observable<ReplyMessage<Boolean>> makeWarningNotification(CommandMessage<NotificationDto> msg) {
    return this.messagePublisher.request(this.getTopic("warning"), JsonUtil.toJson(msg))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  private <T> ReplyMessage<T>  handleReply(Message msg, Class<?> dataType) {
    String json = new String(msg.getData(), StandardCharsets.UTF_8);
    Type replyMessageType = GenericTypeUtil.getType(ReplyMessage.class, dataType);
    return (ReplyMessage<T>) JsonUtil.fromJson(json, replyMessageType);
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.notificationServiceProxy, t);
  }
}
