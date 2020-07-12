package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dto.RawFileDto;
import com.stefan.postservice.dto.UpdatePostDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class UpdatePostMessageHandler extends MessageHandler<UpdatePostDto, Boolean> {
  @Autowired
  private Service service;

  @Autowired
  private FileStorageService fileStorageService;

  @Override
  protected CommandMessage<UpdatePostDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);

    Type type = new TypeToken<CommandMessage<UpdatePostDto>>() {
    }.getType();

    return (CommandMessage<UpdatePostDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<Boolean> getReplyMessage(UpdatePostDto updatePostDto) {
    ReplyMessage<Boolean> replyMessage = new ReplyMessage<>();

    try {
      this.service.updatePost(new Post(updatePostDto.getPost()));
      this.fileStorageService.removeFile(String.valueOf(updatePostDto.getPost().getId()));

      if (updatePostDto.getFiles().size() > 0) {
        for (RawFileDto file : updatePostDto.getFiles()) {
          this.fileStorageService.storeFile(
              file.getFileBytes(),
              file.getFileName(),
              String.valueOf(updatePostDto.getPost().getId())
          );
        }
      }


      replyMessage.setData(true);
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
