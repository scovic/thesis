package com.example.orchestration.saga.iamsagas;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.iamservice.UserDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(String token, UserDto userDto) throws Exception {
    AuthorizeDto authorizeDto = new AuthorizeDto(token);

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> this.iamServiceProxy.authorize(commandMessage),
            authorizeDto
        )
        .addStep(
            commandMessage -> this.iamServiceProxy.updateUser(commandMessage),
            userDto
        ).build();
  }


  public Observable<?> executeSaga() {
    return this.step.executeTransaction();
  }
}
