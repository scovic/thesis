package com.stefan.iam.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.iam.MessageHandler;
import com.stefan.iam.dto.DeleteUserDto;
import com.stefan.iam.dto.UserDto;
import com.stefan.iam.dto.UserIdsDto;
import com.stefan.iam.dto.UsersDto;
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
import java.util.ArrayList;
import java.util.List;

@Component
public class GetUsersMessageHandler extends MessageHandler<UserIdsDto, UsersDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<UserIdsDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<UserIdsDto>>() {
    }.getType();
    return (CommandMessage<UserIdsDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<UsersDto> getReplyMessage(UserIdsDto userIdsDto) {
    ReplyMessage<UsersDto> replyMessage = new ReplyMessage<>();

    try {
      List<UserDto> users = new ArrayList<>();

      for (Integer id : userIdsDto.getUserIds()) {
        UserDto userDto = this.service.getUser(id);
        users.add(userDto);
      }

      replyMessage.setData(new UsersDto(users));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
