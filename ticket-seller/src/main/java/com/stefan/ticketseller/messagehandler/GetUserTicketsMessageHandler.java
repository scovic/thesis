package com.stefan.ticketseller.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.ticketseller.MessageHandler;
import com.stefan.ticketseller.dto.GetUserTicketsReqDto;
import com.stefan.ticketseller.dto.GetUserTicketsRespDto;
import com.stefan.ticketseller.dto.PurchaseDetailsDto;
import com.stefan.ticketseller.message.CommandMessage;
import com.stefan.ticketseller.message.ReplyMessage;
import com.stefan.ticketseller.message.TransactionStatus;
import com.stefan.ticketseller.model.PurchaseDetails;
import com.stefan.ticketseller.service.Service;
import com.stefan.ticketseller.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class GetUserTicketsMessageHandler extends MessageHandler<GetUserTicketsReqDto, GetUserTicketsRespDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<GetUserTicketsReqDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<GetUserTicketsReqDto>>() {}.getType();
    return (CommandMessage<GetUserTicketsReqDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<GetUserTicketsRespDto> getReplyMessage(GetUserTicketsReqDto getUserTicketsReqDto) {
    ReplyMessage<GetUserTicketsRespDto> replyMessage = new ReplyMessage<>();

    try {
      List<PurchaseDetails> list = this.service.getUserTickets(getUserTicketsReqDto.getUserId());
      replyMessage.setData(new GetUserTicketsRespDto(this.convertToDto(list)));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }

  private List<PurchaseDetailsDto> convertToDto(List<PurchaseDetails> list) {
    List<PurchaseDetailsDto> resultList = new ArrayList<>();

    for (PurchaseDetails pd: list) {
      resultList.add(new PurchaseDetailsDto(
          pd.getId(),
          pd.getUserId(),
          pd.getPurchaseDate()
      ));
    }

    return resultList;
  }
}
