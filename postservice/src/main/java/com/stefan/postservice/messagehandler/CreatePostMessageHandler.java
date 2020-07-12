package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dto.PostDto;
import com.stefan.postservice.dto.CreatePostDto;
import com.stefan.postservice.dto.RawFileDto;
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
import java.util.ArrayList;
import java.util.List;

@Component
public class CreatePostMessageHandler extends MessageHandler<CreatePostDto, PostDto> {
  @Autowired
  private Service service;

  @Autowired
  private FileStorageService fileStorageService;

  @Override
  protected CommandMessage<CreatePostDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<CreatePostDto>>() {
    }.getType();
    return (CommandMessage<CreatePostDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<PostDto> getReplyMessage(CreatePostDto createPostDto) {
    ReplyMessage<PostDto> replyMessage = new ReplyMessage<>();

    try {
      PostDto createdPost = service.addPost(new Post(createPostDto.getPost())).getPostData();

      if (createPostDto.getFiles().size() > 0) {
        List<String> attachmentNames = new ArrayList<>();
        for (RawFileDto file : createPostDto.getFiles()) {
          attachmentNames.add(file.getFileName());

          this.fileStorageService.storeFile(
              file.getFileBytes(),
              file.getFileName(),
              String.valueOf(createdPost.getId())
          );
        }

        createdPost.setAttachmentNames(attachmentNames);
      }

      replyMessage.setData(createdPost);

    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
