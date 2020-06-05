package com.example.orchestration.saga;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSata(String token, String email) throws Exception {
    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            new AuthorizeDto(token, email)
        )
        .build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
