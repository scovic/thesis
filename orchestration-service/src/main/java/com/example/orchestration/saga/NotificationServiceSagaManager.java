package com.example.orchestration.saga;

import com.example.orchestration.saga.notificationservice.MakeInfoNotificationSaga;
import com.example.orchestration.saga.notificationservice.MakeWarningNotificationSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationServiceSagaManager {
  @Autowired
  private MakeInfoNotificationSaga makeInfoNotificationSaga;

  @Autowired
  private MakeWarningNotificationSaga makeWarningNotificationSaga;

  public MakeInfoNotificationSaga getMakeInfoNotificationSaga() {
    return makeInfoNotificationSaga;
  }

  public MakeWarningNotificationSaga getMakeWarningNotificationSaga() {
    return makeWarningNotificationSaga;
  }
}
