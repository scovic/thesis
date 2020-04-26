package com.stefan.iam.messagehandler;

import com.stefan.iam.MessagePublisher;
import com.stefan.iam.dto.LoginReqDto;
import com.stefan.iam.dto.LoginRespDto;
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
public class LoginMessageHandler implements MessageHandler {
  @Autowired
  private MessagePublisher messagePublisher;

  @Autowired
  private Service service;

  @Override
  public void onMessage(Message message) throws InterruptedException {
    LoginReqDto loginReqDto = this.getDataFromMessage(message);
    ReplyMessage<LoginRespDto> replyMessage = new ReplyMessage<>();

    try {
      String token = this.service.login(loginReqDto.getEmail(), loginReqDto.getPassword());
      replyMessage.setData(new LoginRespDto(token));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    messagePublisher.publish(message.getReplyTo(), JsonUtil.toJson(replyMessage));
  }

  private LoginReqDto getDataFromMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = GenericTypeUtil.getType(CommandMessage.class, LoginReqDto.class);
    CommandMessage<LoginReqDto> cm = (CommandMessage<LoginReqDto>) JsonUtil.fromJson(json, type);

    return cm.getData();
  }
}
