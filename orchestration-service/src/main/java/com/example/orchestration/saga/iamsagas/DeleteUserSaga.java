package com.example.orchestration.saga.iamsagas;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.iamservice.DeleteUserDto;
import com.example.orchestration.dto.postservice.AuthorDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.PostServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  @Autowired
  private PostServiceProxy postServiceProxy;

  public void initSaga(String token, int userId) throws Exception {
    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            new AuthorizeDto(token)
        )
        .addStep(
            commandMessage -> iamServiceProxy.deleteUser(commandMessage),
            new DeleteUserDto(userId)
        )
        .addStep(
            commandMessage -> this.postServiceProxy.deleteUsersContent(commandMessage),
            new AuthorDto(userId)
        )
        .build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
