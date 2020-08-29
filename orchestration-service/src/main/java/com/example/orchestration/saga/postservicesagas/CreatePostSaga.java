package com.example.orchestration.saga.postservicesagas;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.postservice.CreatePostDto;
import com.example.orchestration.dto.postservice.PostDto;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.PostServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreatePostSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  @Autowired
  private PostServiceProxy postServiceProxy;

  public void initSaga(String token, CreatePostDto createPostDto) throws Exception {
    AuthorizeDto authorizeDto = new AuthorizeDto(token);

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> this.iamServiceProxy.authorize(commandMessage),
            authorizeDto
        )
        .addStep(
            commandMessage -> this.postServiceProxy.createPost(commandMessage),
            compensationCommandMessage -> {

              this.postServiceProxy.deletePost(
                  new CommandMessage<PostDto>(compensationCommandMessage)
              ).subscribe();
            },
            createPostDto
        ).build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
