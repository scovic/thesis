package com.stefan.iam.messagehandler;

import com.stefan.iam.MessagePublisher;
import com.stefan.iam.dto.CreateUserReqDto;
import com.stefan.iam.dto.CreateUserRespDto;
import com.stefan.iam.dto.UserDto;
import com.stefan.iam.message.CommandMessage;
import com.stefan.iam.message.ReplyMessage;
import com.stefan.iam.message.TransactionStatus;
import com.stefan.iam.service.Service;
import com.stefan.iam.util.GenericTypeUtil;
import com.stefan.iam.util.JsonUtil;
import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class CreateUserMessageHandler implements MessageHandler {
  @Autowired
  private MessagePublisher messagePublisher;

  @Autowired
  private Service service;

  @Override
  public void onMessage(Message message) throws InterruptedException {
    CreateUserReqDto createUserReqDto = this.getDataFromMessage(message);
    UserDto userDto = this.userReqDtoToUserDto(createUserReqDto);
    ReplyMessage<CreateUserRespDto> replyMessage = new ReplyMessage<>();

    try {
      UserDto createdUser = service.registerUser(userDto);

      CreateUserRespDto respDto = new CreateUserRespDto();
      respDto.setId(createdUser.getId());

      replyMessage.setData(respDto);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    messagePublisher.publish(message.getReplyTo(), JsonUtil.toJson(replyMessage));
  }


  private UserDto userReqDtoToUserDto(CreateUserReqDto createUserReqDto) {
    UserDto userDto = new UserDto();
    userDto.setEmail(createUserReqDto.getEmail());
    userDto.setPassword(createUserReqDto.getPassword());
    userDto.setFirstName(createUserReqDto.getFirstName());
    userDto.setLastName(createUserReqDto.getLastName());

    return userDto;
  }

  private CreateUserReqDto getDataFromMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = GenericTypeUtil.getType(CommandMessage.class, CreateUserReqDto.class);
    CommandMessage<CreateUserReqDto> cm = (CommandMessage<CreateUserReqDto>) JsonUtil.fromJson(json, type);

    return cm.getData();
  }
}
