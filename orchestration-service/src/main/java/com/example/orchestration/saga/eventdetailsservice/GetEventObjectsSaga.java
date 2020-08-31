package com.example.orchestration.saga.eventdetailsservice;

import com.example.orchestration.dto.EmptyDto;
import com.example.orchestration.dto.eventdetailsservice.EventObjectsDto;
import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.proxy.EventDetailsServiceProxy;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetEventObjectsSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  @Autowired
  private EventDetailsServiceProxy eventDetailsServiceProxy;

  public void initSaga(String token) throws Exception {
    AuthorizeDto authorizeDto = new AuthorizeDto(token);

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> this.iamServiceProxy.authorize(commandMessage),
            authorizeDto
        )
        .<EmptyDto, EventObjectsDto>addStep(
            commandMessage -> this.eventDetailsServiceProxy.getEventObjects(commandMessage),
            new EmptyDto()
        )
        .build();
  }

  public Observable<?> executeSaga() {
    return this.step.executeTransaction();
  }
}
