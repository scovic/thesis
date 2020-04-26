package com.stefan.iam.messagehandler;

import com.stefan.iam.MessagePublisher;
import com.stefan.iam.dto.AuthorizeDto;
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
public class AuthorizeMessageHandler implements MessageHandler {
  @Autowired
  private MessagePublisher messagePublisher;

  @Autowired
  private Service service;

  @Override
  public void onMessage(Message message) throws InterruptedException {
    AuthorizeDto authorizeDto = this.getDataFromMessage(message);
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

    this.messagePublisher.publish(message.getReplyTo(), JsonUtil.toJson(replyMessage));
  }

  private AuthorizeDto getDataFromMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = GenericTypeUtil.getType(CommandMessage.class, AuthorizeDto.class);
    CommandMessage<AuthorizeDto> cm = (CommandMessage<AuthorizeDto>) JsonUtil.fromJson(json, type);

    return cm.getData();
  }
}
