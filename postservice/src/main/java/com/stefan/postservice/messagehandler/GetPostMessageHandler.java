package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dao.PostDao;
import com.stefan.postservice.dto.CreatePostDto;
import com.stefan.postservice.dto.PostDto;
import com.stefan.postservice.message.CommandMessage;
import com.stefan.postservice.message.ReplyMessage;
import com.stefan.postservice.message.TransactionStatus;
import com.stefan.postservice.service.FileStorageService;
import com.stefan.postservice.service.Service;
import com.stefan.postservice.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class GetPostMessageHandler extends MessageHandler<Integer, PostDto> {
  @Autowired
  private Service service;

  @Autowired
  private FileStorageService fileStorageService;

  @Override
  protected CommandMessage<Integer> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<Integer>>() {
    }.getType();

    return (CommandMessage<Integer>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<PostDto> getReplyMessage(Integer postId) {
    ReplyMessage<PostDto> replyMessage = new ReplyMessage<>();

    try {
      PostDto post = this.service.getPost(postId.intValue()).getPostData();

      post.setAttachmentNames(
          this.fileStorageService.listDirFileNames(String.valueOf(post.getId()))
      );

      replyMessage.setData(post);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}