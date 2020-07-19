package com.example.orchestration.proxy;

import com.example.orchestration.MessagePublisher;
import com.example.orchestration.dto.iamservice.*;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.util.GenericTypeUtil;
import com.example.orchestration.util.JsonUtil;
import io.nats.client.Message;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class IamServiceProxy {
  private String serviceTopic;
  private MessagePublisher messagePublisher;

  @Autowired
  public IamServiceProxy(MessagePublisher messagePublisher) {
    this.serviceTopic = "iam-service-";
    this.messagePublisher = messagePublisher;
  }

  public Observable<ReplyMessage<CreateUserReplyDto>> createUser(CommandMessage<UserDto> msg) {
    return this.messagePublisher.request(this.getTopic("create"), JsonUtil.toJson(msg))
        .map(message -> this.<CreateUserReplyDto>handleReply(message, CreateUserReplyDto.class));
  }

  public Observable<ReplyMessage<Boolean>> deleteUser(CommandMessage<DeleteUserDto> msg) {
    return this.messagePublisher.request(this.getTopic("delete"), JsonUtil.toJson(msg))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  public Observable<ReplyMessage<LoginResponseDto>> login(CommandMessage<LoginRequestDto> dto) {
    return this.messagePublisher.request(this.getTopic("login"), JsonUtil.toJson(dto))
        .map(message-> this.<LoginResponseDto>handleReply(message, LoginResponseDto.class));
  }

  public Observable<ReplyMessage<Boolean>> authorize(CommandMessage<AuthorizeDto> dto) {
    return this.messagePublisher.request(this.getTopic("authorize"), JsonUtil.toJson(dto))
        .map(message-> this.<Boolean>handleReply(message, Boolean.class));
  }

  public Observable<ReplyMessage<UsersDto>> getUsers(CommandMessage<UserIdsDto> dto) {
    return this.messagePublisher.request(this.getTopic("get-users"), JsonUtil.toJson(dto))
        .map(message-> this.<UsersDto>handleReply(message, UsersDto.class));
  }

  private <T> ReplyMessage<T> handleReply(Message msg, Class<?> dataType) {
    String json = new String(msg.getData(), StandardCharsets.UTF_8);
    Type replyMessageType = GenericTypeUtil.getType(ReplyMessage.class, dataType);

    return (ReplyMessage<T>) JsonUtil.fromJson(json, replyMessageType);
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.serviceTopic, t);
  }
}
