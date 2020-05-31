package com.stefan.ticketseller.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.ticketseller.MessageHandler;
import com.stefan.ticketseller.dto.CancelPurchaseReqDto;
import com.stefan.ticketseller.message.CommandMessage;
import com.stefan.ticketseller.message.ReplyMessage;
import com.stefan.ticketseller.message.TransactionStatus;
import com.stefan.ticketseller.service.Service;
import com.stefan.ticketseller.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class CancelPurchaseMessageHandler extends MessageHandler<CancelPurchaseReqDto, Boolean> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<CancelPurchaseReqDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<CancelPurchaseReqDto>>() {}.getType();
    return (CommandMessage<CancelPurchaseReqDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(CancelPurchaseReqDto cancelPurchaseReqDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      for (Integer id : cancelPurchaseReqDto.getIds()) {
        this.service.cancelPurchase(id.intValue());
      }

      replyMessage.setData(true);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
