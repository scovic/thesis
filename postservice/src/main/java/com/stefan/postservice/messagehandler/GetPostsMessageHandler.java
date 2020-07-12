package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dto.EmptyDto;
import com.stefan.postservice.dto.GetPostsDto;
import com.stefan.postservice.dto.PostDto;
import com.stefan.postservice.message.CommandMessage;
import com.stefan.postservice.message.ReplyMessage;
import com.stefan.postservice.message.TransactionStatus;
import com.stefan.postservice.model.Post;
import com.stefan.postservice.service.FileStorageService;
import com.stefan.postservice.service.Service;
import com.stefan.postservice.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class GetPostsMessageHandler extends MessageHandler<EmptyDto, GetPostsDto> {
  @Autowired
  private Service service;

  @Autowired
  private FileStorageService fileStorageService;

  @Override
  protected CommandMessage<EmptyDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<EmptyDto>>() {
    }.getType();

    return (CommandMessage<EmptyDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<GetPostsDto> getReplyMessage(EmptyDto emptyDto) {
    ReplyMessage<GetPostsDto> replyMessage = new ReplyMessage<>();

    try {
      List<PostDto> posts = Post.convertPostListToPostDtoList(this.service.getAll());


      for (PostDto post : posts) {
        post.setAttachmentNames(
            this.fileStorageService.listDirFileNames(String.valueOf(post.getId()))
        );
      }

      replyMessage.setData(new GetPostsDto(posts));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
