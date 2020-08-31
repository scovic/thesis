package com.example.eventdetailsservice.messagehandler;

import com.example.eventdetailsservice.MessageHandler;
import com.example.eventdetailsservice.dto.EmptyDto;
import com.example.eventdetailsservice.dto.EventObjectDto;
import com.example.eventdetailsservice.dto.EventObjectsDto;
import com.example.eventdetailsservice.message.CommandMessage;
import com.example.eventdetailsservice.message.ReplyMessage;
import com.example.eventdetailsservice.model.EventObject;
import com.example.eventdetailsservice.service.Service;
import com.example.eventdetailsservice.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class GetEventObjectsMessageHandler extends MessageHandler<EmptyDto, EventObjectsDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<EmptyDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<EmptyDto>>() {
    }.getType();

    return (CommandMessage<EmptyDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<EventObjectsDto> getReplyMessage(EmptyDto emptyDto) {
    ReplyMessage<EventObjectsDto> replyMessage = new ReplyMessage<>();

    List<EventObject> eventObjects = service.getEventsObjects();
    List<EventObjectDto> eventObjectDtos = new ArrayList<>();

    for (EventObject eventObject : eventObjects) {
      eventObjectDtos.add(eventObject.getData());
    }

    replyMessage.setData(new EventObjectsDto(eventObjectDtos));

    return replyMessage;
  }
}
