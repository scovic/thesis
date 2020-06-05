package com.example.orchestration.saga;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.iamservice.DeleteUserDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(String token, String email, int userId) throws Exception {
    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            new AuthorizeDto(token, email)
        )
        .addStep(
            commandMessage -> iamServiceProxy.deleteUser(commandMessage),
            new DeleteUserDto(userId)
        )
        .build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
