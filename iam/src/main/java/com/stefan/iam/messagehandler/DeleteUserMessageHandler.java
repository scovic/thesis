package com.stefan.iam.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.iam.MessageHandler;
import com.stefan.iam.dto.DeleteUserDto;
import com.stefan.iam.dto.LoginReqDto;
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
public class DeleteUserMessageHandler extends MessageHandler<DeleteUserDto, Boolean> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<DeleteUserDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<DeleteUserDto>>() {}.getType();
    return (CommandMessage<DeleteUserDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(DeleteUserDto deleteUserDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      boolean isDeleted = this.service.deleteUser(deleteUserDto.getId());
      replyMessage.setData(isDeleted);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
