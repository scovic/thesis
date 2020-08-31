package com.example.eventdetailsservice.messagehandler;

import com.example.eventdetailsservice.MessageHandler;
import com.example.eventdetailsservice.dto.EmptyDto;
import com.example.eventdetailsservice.dto.GetStagePerformersDto;
import com.example.eventdetailsservice.dto.PerformerDto;
import com.example.eventdetailsservice.dto.PerformersDto;
import com.example.eventdetailsservice.message.CommandMessage;
import com.example.eventdetailsservice.message.ReplyMessage;
import com.example.eventdetailsservice.message.TransactionStatus;
import com.example.eventdetailsservice.model.Performer;
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
public class GetStagePerformersMessageHandler extends MessageHandler<GetStagePerformersDto, PerformersDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<GetStagePerformersDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<GetStagePerformersDto>>() {
    }.getType();

    return (CommandMessage<GetStagePerformersDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<PerformersDto> getReplyMessage(GetStagePerformersDto stageIdDto) {
    ReplyMessage<PerformersDto> replyMessage = new ReplyMessage<>();

    try {
      List<Performer> performerList = service.getStagePerformers(stageIdDto.getStageId());
      List<PerformerDto> performerDtos = new ArrayList<>();

      for (Performer performer : performerList) {
        performerDtos.add(performer.getData());
      }

      replyMessage.setData(new PerformersDto(performerDtos));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }


    return replyMessage;
  }
}
