package com.example.orchestration.proxy;

import com.example.orchestration.MessagePublisher;
import com.example.orchestration.dto.EmptyDto;
import com.example.orchestration.dto.postservice.*;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.util.GenericTypeUtil;
import com.example.orchestration.util.JsonUtil;
import io.nats.client.Message;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class PostServiceProxy {
  private String serviceProxy;
  private MessagePublisher messagePublisher;

  @Autowired
  public PostServiceProxy(MessagePublisher messagePublisher) {
    this.serviceProxy = "post-service-";
    this.messagePublisher = messagePublisher;
  }

  public Observable<ReplyMessage<PostsDto>> getPosts(CommandMessage<EmptyDto> msg) {
    return this.messagePublisher.request(this.getTopic("get-all-posts"), JsonUtil.toJson(msg))
        .map(message -> this.<PostsDto>handleReply(message, PostsDto.class));
  }

  public Observable<ReplyMessage<PostsCommentsDto>> getPostsComments(CommandMessage<Integer> msg) {
    return this.messagePublisher.request(this.getTopic("get-posts-comments"), JsonUtil.toJson(msg))
        .map(message -> this.<PostsCommentsDto>handleReply(message, PostsCommentsDto.class));
  }

  public Observable<ReplyMessage<PostDto>> getPost(CommandMessage<Integer> msg) {
    return this.messagePublisher.request(this.getTopic("get-post"), JsonUtil.toJson(msg))
        .map(message -> this.<PostDto>handleReply(message, PostDto.class));
  }

  public Observable<ReplyMessage<PostDto>> createPost(CommandMessage<CreatePostDto> msg) {
    return this.messagePublisher.request(this.getTopic("create-post"), JsonUtil.toJson(msg))
        .map(message -> this.<PostDto>handleReply(message, PostDto.class));
  }

  public Observable<ReplyMessage<Boolean>> updatePost(CommandMessage<UpdatePostDto> msg) {
    return this.messagePublisher.request(this.getTopic("update-post"), JsonUtil.toJson(msg))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  public Observable<ReplyMessage<Boolean>> deletePost(CommandMessage<PostDto> msg) {
    return this.messagePublisher.request(this.getTopic("delete-post"), JsonUtil.toJson(msg))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  public Observable<ReplyMessage<CommentDto>> createComment(CommandMessage<CommentDto> msg) {
    return this.messagePublisher.request(this.getTopic("create-comment"), JsonUtil.toJson(msg))
        .map(message -> this.<CommentDto>handleReply(message, CommentDto.class));
  }

  public Observable<ReplyMessage<Boolean>> updateComment(CommandMessage<CommentDto> msg) {
    return this.messagePublisher.request(this.getTopic("update-comment"), JsonUtil.toJson(msg))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  public Observable<ReplyMessage<Boolean>> deleteComment(CommandMessage<CommentDto> msg) {
    return this.messagePublisher.request(this.getTopic("delete-comment"), JsonUtil.toJson(msg))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  public Observable<ReplyMessage<RawFileDto>> getFile(CommandMessage<FileDto> msg) {
    return this.messagePublisher.request(this.getTopic("get-file"), JsonUtil.toJson(msg))
        .map(message -> this.<RawFileDto>handleReply(message, RawFileDto.class));
  }

  public Observable<ReplyMessage<Boolean>> deleteFile(CommandMessage<FileDto> msg) {
    return this.messagePublisher.request(this.getTopic("delete-file"), JsonUtil.toJson(msg))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  public Observable<ReplyMessage<Boolean>> deleteUsersContent(CommandMessage<AuthorDto> author) {
    return this.messagePublisher.request(this.getTopic("remove-authors-content"), JsonUtil.toJson(author))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }

  private <T> ReplyMessage<T>  handleReply(Message msg, Class<?> dataType)  {
    String json = new String(msg.getData(), StandardCharsets.UTF_8);
    Type replyMessageType = GenericTypeUtil.getType(ReplyMessage.class, dataType);

    return (ReplyMessage<T>) JsonUtil.fromJson(json, replyMessageType);
  }

  private String getTopic(String t) {
    return String.format("%s%s",  this.serviceProxy, t);
  }
}
