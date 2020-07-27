package com.example.orchestration.saga.postservicesagas;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.postservice.CommentDto;
import com.example.orchestration.dto.postservice.PostDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.PostServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetPostsCommentsSaga {
  private Step step;

  @Autowired
  private IamServiceProxy  iamServiceProxy;

  @Autowired
  private PostServiceProxy postServiceProxy;

  public void initSaga(String token, int postId) throws Exception {
    AuthorizeDto authorizeDto = new AuthorizeDto(token);

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            authorizeDto
        )
        .addStep(
            commandMessage -> postServiceProxy.getPostsComments(commandMessage),
            postId
        )
        .build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}