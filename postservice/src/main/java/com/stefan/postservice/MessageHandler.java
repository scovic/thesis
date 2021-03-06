package com.stefan.postservice;

import com.stefan.postservice.message.CommandMessage;
import com.stefan.postservice.message.ReplyMessage;
import com.stefan.postservice.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class MessageHandler<T, N> implements io.nats.client.MessageHandler {
  @Autowired
  private MessagePublisher messagePublisher;

  @Override
  public void onMessage(Message message) throws InterruptedException {
    CommandMessage<T> commandMessage = this.getCommandMessage(message);
    ReplyMessage<N> replyMessage = this.getReplyMessage(commandMessage.getData());
    try {

      this.messagePublisher.publish(message.getReplyTo(), JsonUtil.toJson(replyMessage));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }

  }

  protected abstract CommandMessage<T> getCommandMessage(Message message);
  protected abstract ReplyMessage<N> getReplyMessage(T t);
}
