package com.example.orchestration.saga.iamsagas;

import com.example.orchestration.dto.iamservice.LoginRequestDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(LoginRequestDto loginRequestDto) throws Exception {

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> this.iamServiceProxy.login(commandMessage),
            loginRequestDto
        ).build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
