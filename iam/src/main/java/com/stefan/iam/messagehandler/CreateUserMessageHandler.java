package com.stefan.iam.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.iam.MessageHandler;
import com.stefan.iam.dto.CreateUserReqDto;
import com.stefan.iam.dto.CreateUserRespDto;
import com.stefan.iam.dto.DeleteUserDto;
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
public class CreateUserMessageHandler extends MessageHandler<CreateUserReqDto, CreateUserRespDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<CreateUserReqDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<CreateUserReqDto>>() {}.getType();
    return (CommandMessage<CreateUserReqDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<CreateUserRespDto> getReplyMessage(CreateUserReqDto createUserReqDto) {
    ReplyMessage<CreateUserRespDto> replyMessage = new ReplyMessage<>();
    UserDto userDto = this.userReqDtoToUserDto(createUserReqDto);

    try {
      UserDto createdUser = service.registerUser(userDto);

      CreateUserRespDto respDto = new CreateUserRespDto();
      respDto.setId(createdUser.getId());

      replyMessage.setData(respDto);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }

  private UserDto userReqDtoToUserDto(CreateUserReqDto createUserReqDto) {
    UserDto userDto = new UserDto();
    userDto.setEmail(createUserReqDto.getEmail());
    userDto.setPassword(createUserReqDto.getPassword());
    userDto.setFirstName(createUserReqDto.getFirstName());
    userDto.setLastName(createUserReqDto.getLastName());

    return userDto;
  }
}
