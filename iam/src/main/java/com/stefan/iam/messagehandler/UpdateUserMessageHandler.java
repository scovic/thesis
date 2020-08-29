package com.stefan.iam.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.iam.MessageHandler;
import com.stefan.iam.dto.UserDto;
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
public class UpdateUserMessageHandler extends MessageHandler<UserDto, Boolean> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<UserDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<UserDto>>() {}.getType();
    return (CommandMessage<UserDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(UserDto userDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      Boolean success = service.updateUser(userDto);
      replyMessage.setData(success);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
