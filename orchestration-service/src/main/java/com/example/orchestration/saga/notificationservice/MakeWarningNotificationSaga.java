package com.example.orchestration.saga.notificationservice;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.notificationservice.NotificationDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.NotificationServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MakeWarningNotificationSaga {
  private Step step;
  @Autowired
  private NotificationServiceProxy notificationServiceProxy;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(String token, NotificationDto notificationDto) throws Exception {
    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            new AuthorizeDto(token)
        )
        .addStep(
            commandMessage -> notificationServiceProxy.makeWarningNotification(commandMessage),
            notificationDto
        )
        .build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
