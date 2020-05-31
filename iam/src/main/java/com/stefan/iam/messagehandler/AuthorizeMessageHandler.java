package com.stefan.iam.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.iam.MessageHandler;
import com.stefan.iam.dto.AuthorizeDto;
import com.stefan.iam.dto.CreateUserReqDto;
import com.stefan.iam.message.CommandMessage;
import com.stefan.iam.message.ReplyMessage;
import com.stefan.iam.message.TransactionStatus;
import com.stefan.iam.service.Service;
import com.stefan.iam.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class AuthorizeMessageHandler extends MessageHandler<AuthorizeDto, Boolean> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<AuthorizeDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<AuthorizeDto>>() {}.getType();
    return (CommandMessage<AuthorizeDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(AuthorizeDto authorizeDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      boolean isAuthorized = this.service.authenticate(authorizeDto.getToken(), authorizeDto.getEmail());
      if (!isAuthorized) {
        replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
      }

      replyMessage.setData(isAuthorized);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
