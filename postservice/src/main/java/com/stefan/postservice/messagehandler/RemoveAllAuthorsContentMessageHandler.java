package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dto.AuthorDto;
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
public class RemoveAllAuthorsContentMessageHandler extends MessageHandler<AuthorDto, Boolean> {
  @Autowired
  private Service service;

  @Autowired
  private FileStorageService fileStorageService;

  @Override
  protected CommandMessage<AuthorDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<AuthorDto>>() {
    }.getType();

    return (CommandMessage<AuthorDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(AuthorDto authorDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      List<Post> posts = this.service.getAuthorsPosts(authorDto.getId());
      this.service.deleteAuthorsPostsAndComments(authorDto.getId());

      for (Post post : posts) {
        this.fileStorageService.removeFile(String.valueOf(post.getId()));
      }

      replyMessage.setData(true);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
