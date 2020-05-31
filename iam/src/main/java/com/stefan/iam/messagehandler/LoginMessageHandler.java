package com.stefan.iam.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.iam.MessageHandler;
import com.stefan.iam.dto.LoginReqDto;
import com.stefan.iam.dto.LoginRespDto;
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
public class LoginMessageHandler extends MessageHandler<LoginReqDto, LoginRespDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<LoginReqDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<LoginReqDto>>() {}.getType();
    return (CommandMessage<LoginReqDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<LoginRespDto> getReplyMessage(LoginReqDto loginReqDto) {
    ReplyMessage<LoginRespDto> replyMessage = new ReplyMessage<>();

    try {
      String token = this.service.login(loginReqDto.getEmail(), loginReqDto.getPassword());
      replyMessage.setData(new LoginRespDto(token));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
