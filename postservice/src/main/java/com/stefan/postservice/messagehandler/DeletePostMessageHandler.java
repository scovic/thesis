package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
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
public class DeletePostMessageHandler extends MessageHandler<PostDto, Boolean> {
  @Autowired
  private Service service;

  @Autowired
  private FileStorageService fileStorageService;

  @Override
  protected CommandMessage<PostDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);

    Type type = new TypeToken<CommandMessage<PostDto>>() {
    }.getType();

    return (CommandMessage<PostDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(PostDto postDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      this.service.deletePost(postDto.getId());
      this.fileStorageService.removeFile(String.valueOf(postDto.getId()));

      replyMessage.setData(true);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
