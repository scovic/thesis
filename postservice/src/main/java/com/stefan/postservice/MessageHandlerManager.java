package com.stefan.postservice;

import com.google.gson.internal.$Gson$Preconditions;
import com.stefan.postservice.messagehandler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerManager {
  private final String topic;

  @Autowired
  public MessageHandlerManager(
      MessageSubscriber messageSubscriber,
      GetPostsMessageHandler getPostsMessageHandler,
      GetPostMessageHandler getPostMessageHandler,
      CreatePostMessageHandler createPostMessageHandler,
      UpdatePostMessageHandler updatePostMessageHandler,
      DeletePostMessageHandler deletePostMessageHandler,
      CreateCommentMessageHandler createCommentMessageHandler,
      UpdateCommentMessageHandler updateCommentMessageHandler,
      DeleteCommentMessageHandler deleteCommentMessageHandler,
      GetFileMessageHandler getFileMessageHandler,
      DeleteFileMessageHandler deleteFileMessageHandler,
      RemoveAllAuthorsContentMessageHandler removeAllAuthorsContentMessageHandler,
      GetPostsCommentsMessageHandler getPostsCommentsMessageHandler
  ) {
    this.topic = "post-service-";

    // Posts
    messageSubscriber.subscribe(this.getTopic("get-all-posts"), getPostsMessageHandler);
    messageSubscriber.subscribe(this.getTopic("get-post"), getPostMessageHandler);
    messageSubscriber.subscribe(this.getTopic("create-post"), createPostMessageHandler);
    messageSubscriber.subscribe(this.getTopic("update-post"), updatePostMessageHandler);
    messageSubscriber.subscribe(this.getTopic("delete-post"), deletePostMessageHandler);

    // Comments
    messageSubscriber.subscribe(this.getTopic("create-comment"), createCommentMessageHandler);
    messageSubscriber.subscribe(this.getTopic("update-comment"), updateCommentMessageHandler);
    messageSubscriber.subscribe(this.getTopic("delete-comment"), deleteCommentMessageHandler);

    // Files
    messageSubscriber.subscribe(this.getTopic("get-file"), getFileMessageHandler);
    messageSubscriber.subscribe(this.getTopic("delete-file"), deleteFileMessageHandler);

    // Others
    messageSubscriber.subscribe(
        this.getTopic("remove-authors-content"),
        removeAllAuthorsContentMessageHandler
    );

    messageSubscriber.subscribe(
        this.getTopic("get-posts-comments"),
        getPostsCommentsMessageHandler
    );
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.topic, t);
  }
}
