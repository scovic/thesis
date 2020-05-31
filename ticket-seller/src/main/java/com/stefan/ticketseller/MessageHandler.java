package com.stefan.ticketseller;

import com.stefan.ticketseller.message.CommandMessage;
import com.stefan.ticketseller.message.ReplyMessage;
import com.stefan.ticketseller.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class MessageHandler<T, N> implements io.nats.client.MessageHandler {
  @Autowired
  private MessagePublisher messagePublisher;

  @Override
  public void onMessage(Message message) throws InterruptedException {
    CommandMessage<T> commandMessage = this.getCommandMessage(message);
    ReplyMessage<N> replyMessage = this.getReplyMessage(commandMessage.getData());
    this.messagePublisher.publish(message.getReplyTo(), JsonUtil.toJson(replyMessage));
  }

  protected abstract CommandMessage<T> getCommandMessage(Message message);
  protected abstract ReplyMessage<N> getReplyMessage(T t);
}
