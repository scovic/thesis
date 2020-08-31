package com.example.orchestration.proxy;

import com.example.orchestration.MessagePublisher;
import com.example.orchestration.dto.EmptyDto;
import com.example.orchestration.dto.eventdetailsservice.EventObjectsDto;
import com.example.orchestration.dto.eventdetailsservice.GetStagePerformersDto;
import com.example.orchestration.dto.eventdetailsservice.PerformersDto;
import com.example.orchestration.dto.iamservice.UsersDto;
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
public class EventDetailsServiceProxy {
  private String serviceTopic;
  private MessagePublisher messagePublisher;

  @Autowired
  public EventDetailsServiceProxy(MessagePublisher messagePublisher) {
    this.serviceTopic = "event-details-service-";
    this.messagePublisher = messagePublisher;
  }

  public Observable<ReplyMessage<EventObjectsDto>> getEventObjects(CommandMessage<EmptyDto> dto) {
    return this.messagePublisher.request(this.getTopic("get-event-objects"), JsonUtil.toJson(dto))
        .map(message -> this.<EventObjectsDto>handleReply(message, EventObjectsDto.class));
  }

  public Observable<ReplyMessage<PerformersDto>> getStagePerformers(CommandMessage<GetStagePerformersDto> dto) {
    return this.messagePublisher.request(this.getTopic("get-stage-performers"), JsonUtil.toJson(dto))
        .map(message -> this.<PerformersDto>handleReply(message, PerformersDto.class));
  }

  private <T> ReplyMessage<T> handleReply(Message msg, Class<?> dataType) {
    String json = new String(msg.getData(), StandardCharsets.UTF_8);
    Type replyMessageType = GenericTypeUtil.getType(ReplyMessage.class, dataType);

    return (ReplyMessage<T>) JsonUtil.fromJson(json, replyMessageType);
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.serviceTopic, t);
  }
}
