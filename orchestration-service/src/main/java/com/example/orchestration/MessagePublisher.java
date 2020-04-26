package com.example.orchestration;

import com.example.orchestration.dto.iamservice.CreateUserReplyDto;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.util.GenericTypeUtil;
import com.example.orchestration.util.JsonUtil;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Message;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {
  @Autowired
  private Connection natsConnection;

  public Observable<Message> request(String topic, String message) {
    return Observable.create(subscriber -> {
      natsConnection.request(topic, message.getBytes())
          .whenComplete((msg, error) -> {
            if (error != null) {
              subscriber.onError(error);
            } else {
              subscriber.onNext(msg);
              subscriber.onComplete();
            }
          });
    });
  }
}
