package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dto.FileDto;
import com.stefan.postservice.message.CommandMessage;
import com.stefan.postservice.message.ReplyMessage;
import com.stefan.postservice.message.TransactionStatus;
import com.stefan.postservice.service.FileStorageService;
import com.stefan.postservice.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class DeleteFileMessageHandler extends MessageHandler<FileDto, Boolean> {
  @Autowired
  private FileStorageService fileStorageService;

  @Override
  protected CommandMessage<FileDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);

    Type type = new TypeToken<CommandMessage<FileDto>>() {
    }.getType();

    return (CommandMessage<FileDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(FileDto fileDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      this.fileStorageService.removeFile(String.format("%s/%s", fileDto.getPostId(), fileDto.getFileName()));

      replyMessage.setData(true);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
