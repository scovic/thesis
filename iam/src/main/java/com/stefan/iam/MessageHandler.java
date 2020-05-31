package com.stefan.iam;
import com.stefan.iam.message.CommandMessage;
import com.stefan.iam.message.ReplyMessage;
import com.stefan.iam.util.JsonUtil;
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
    this.messagePublisher.publish(message.getReplyTo(), JsonUtil.toJson(replyMessage));
  }

  protected abstract CommandMessage<T> getCommandMessage (Message message);
  protected abstract ReplyMessage<N> getReplyMessage(T t);
}
