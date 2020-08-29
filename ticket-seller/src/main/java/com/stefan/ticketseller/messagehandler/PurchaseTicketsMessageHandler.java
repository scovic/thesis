package com.stefan.ticketseller.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.ticketseller.MessageHandler;
import com.stefan.ticketseller.dto.PurchaseDetailsDto;
import com.stefan.ticketseller.dto.PurchaseTicketsReqDto;
import com.stefan.ticketseller.dto.PurchaseTicketsRespDto;
import com.stefan.ticketseller.message.CommandMessage;
import com.stefan.ticketseller.message.ReplyMessage;
import com.stefan.ticketseller.message.TransactionStatus;
import com.stefan.ticketseller.model.PurchaseDetails;
import com.stefan.ticketseller.service.Service;
import com.stefan.ticketseller.util.JsonUtil;
import io.nats.client.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseTicketsMessageHandler extends MessageHandler<PurchaseTicketsReqDto, PurchaseTicketsRespDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<PurchaseTicketsReqDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<PurchaseTicketsReqDto>>() {}.getType();
    return (CommandMessage<PurchaseTicketsReqDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<PurchaseTicketsRespDto> getReplyMessage(PurchaseTicketsReqDto purchaseTicketsReqDto) {
    ReplyMessage<PurchaseTicketsRespDto> replyMessage = new ReplyMessage<>();

    try {
      List<PurchaseDetails> list = this.service.purchaseTicket(
          purchaseTicketsReqDto.getUserId(),
          purchaseTicketsReqDto.getQuantity()
      );

      replyMessage.setData(new PurchaseTicketsRespDto(this.convertToDto(list)));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }


  private @NotNull List<PurchaseDetailsDto> convertToDto(@NotNull List<PurchaseDetails> list) {
    List<PurchaseDetailsDto> resultList = new ArrayList<>();

    for (PurchaseDetails pd : list) {
      resultList.add(new PurchaseDetailsDto(
          pd.getId(),
          pd.getUserId(),
          pd.getPurchaseDate().getTime()
      ));
    }

    return resultList;
  }
}
