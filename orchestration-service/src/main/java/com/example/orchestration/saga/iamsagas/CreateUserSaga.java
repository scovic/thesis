package com.example.orchestration.saga.iamsagas;

import com.example.orchestration.dto.iamservice.UserDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateUserSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(UserDto userDto) throws Exception {
    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.createUser(commandMessage),
            userDto
        ).build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
