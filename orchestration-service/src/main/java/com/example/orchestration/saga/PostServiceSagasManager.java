package com.example.orchestration.saga;

import com.example.orchestration.saga.postservicesagas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostServiceSagasManager {
  @Autowired
  private GetPostsSaga getPostsSaga;

  @Autowired
  private GetPostSaga getPostSaga;

  @Autowired
  private CreatePostSaga createPostSaga;

  @Autowired
  private UpdatePostSaga updatePostSaga;

  @Autowired
  private DeletePostSaga deletePostSaga;

  @Autowired
  private CreateCommentSaga createCommentSaga;

  @Autowired
  private UpdateCommentSaga updateCommentSaga;

  @Autowired
  private DeleteCommentSaga deleteCommentSaga;

  @Autowired
  private GetFileSaga getFileSaga;

  @Autowired
  private DeleteFileSaga deleteFileSaga;

  @Autowired
  private GetPostsCommentsSaga getPostsCommentsSaga;

  public GetPostsSaga getGetPostsSaga() {
    return getPostsSaga;
  }

  public GetPostSaga getGetPostSaga() {
    return getPostSaga;
  }

  public CreatePostSaga getCreatePostSaga() {
    return createPostSaga;
  }

  public UpdatePostSaga getUpdatePostSaga() {
    return updatePostSaga;
  }

  public DeletePostSaga getDeletePostSaga() {
    return deletePostSaga;
  }

  public CreateCommentSaga getCreateCommentSaga() {
    return createCommentSaga;
  }

  public UpdateCommentSaga getUpdateCommentSaga() {
    return updateCommentSaga;
  }

  public DeleteCommentSaga getDeleteCommentSaga() {
    return deleteCommentSaga;
  }

  public GetFileSaga getGetFileSaga() {
    return getFileSaga;
  }

  public DeleteFileSaga getDeleteFileSaga() {
    return deleteFileSaga;
  }

  public GetPostsCommentsSaga getGetPostsCommentsSaga() {
    return getPostsCommentsSaga;
  }
}
