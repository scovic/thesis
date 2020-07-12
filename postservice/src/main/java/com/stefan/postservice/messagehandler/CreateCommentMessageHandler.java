package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dto.CommentDto;
import com.stefan.postservice.dto.CreatePostDto;
import com.stefan.postservice.message.CommandMessage;
import com.stefan.postservice.message.ReplyMessage;
import com.stefan.postservice.message.TransactionStatus;
import com.stefan.postservice.model.Comment;
import com.stefan.postservice.service.Service;
import com.stefan.postservice.util.JsonUtil;
import io.nats.client.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class CreateCommentMessageHandler extends MessageHandler<CommentDto, CommentDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<CommentDto> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);

    Type type = new TypeToken<CommandMessage<CommentDto>>() {
    }.getType();

    return (CommandMessage<CommentDto>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<CommentDto> getReplyMessage(CommentDto commentDto) {
    ReplyMessage<CommentDto> replyMessage = new ReplyMessage<>();

    try {
      replyMessage.setData(
          this.service.addComment(new Comment(commentDto)).getCommentData()
      );
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
