package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dto.FileDto;
import com.stefan.postservice.dto.RawFileDto;
import com.stefan.postservice.message.CommandMessage;
import com.stefan.postservice.message.ReplyMessage;
import com.stefan.postservice.message.TransactionStatus;
import com.stefan.postservice.service.FileStorageService;
import com.stefan.postservice.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class GetFileMessageHandler extends MessageHandler<FileDto, RawFileDto> {
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
  protected ReplyMessage<RawFileDto> getReplyMessage(FileDto fileDto) {
    ReplyMessage<RawFileDto> replyMessage = new ReplyMessage<>();

    try {
      replyMessage.setData(new RawFileDto(
          this.fileStorageService.getBytesFromFile(fileDto.getFileName(), String.valueOf(fileDto.getPostId())),
          fileDto.getFileName()
      ));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
