package com.stefan.postservice.messagehandler;

import com.google.gson.reflect.TypeToken;
import com.stefan.postservice.MessageHandler;
import com.stefan.postservice.dto.CommentDto;
import com.stefan.postservice.dto.PostDto;
import com.stefan.postservice.dto.PostsCommentsDto;
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
import java.util.ArrayList;
import java.util.List;

@Component
public class GetPostsCommentsMessageHandler  extends MessageHandler<Integer, PostsCommentsDto> {
  @Autowired
  private Service service;

  @Override
  protected CommandMessage<Integer> getCommandMessage(Message message) {
    String json = new String(message.getData(), StandardCharsets.UTF_8);
    Type type = new TypeToken<CommandMessage<Integer>>() {
    }.getType();

    return (CommandMessage<Integer>) JsonUtil.fromJson(json, type);
  }

  @Override
  protected ReplyMessage<PostsCommentsDto> getReplyMessage(Integer postId) {
    ReplyMessage<PostsCommentsDto> replyMessage = new ReplyMessage<>();

    try {
      List<Comment> commentList = this.service.getPostsComments(postId);

      List<CommentDto> commentDtos = new ArrayList<>();

      for (Comment comment : commentList) {
        commentDtos.add(comment.getCommentData());
      }

      replyMessage.setData(new PostsCommentsDto(
          commentDtos,
          postId
      ));
    } catch (Exception ex) {
      replyMessage.setTransactionStatus(TransactionStatus.FAILURE);
    }

    return replyMessage;
  }
}
