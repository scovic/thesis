package com.example.notificationsservice.service;

import com.example.notificationsservice.model.Notification;
import com.pusher.pushnotifications.PushNotifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceImpl implements Service {
  @Autowired
  private PushNotifications beamsClient;

  @Override
  public void publishInfoNotification(Notification notification) throws IOException, InterruptedException, URISyntaxException {
    List<String> interests = Arrays.asList("info");
    this.publishReq(notification, interests);
  }

  @Override
  public void publishWarningNotification(Notification notification) throws IOException, InterruptedException, URISyntaxException {
    List<String> interests = Arrays.asList("warning");
    this.publishReq(notification, interests);
  }

  private void publishReq(Notification notification, List<String> interests) throws IOException, InterruptedException, URISyntaxException {
    Map<String, Map> publishRequest = new HashMap<>();

    Map<String, String> fcmNotification = new HashMap<>();
    fcmNotification.put("title", notification.getTitle());
    fcmNotification.put("body", notification.getBody());

    Map<String, Map> fcm = new HashMap<>();
    fcm.put("notification", fcmNotification);

    if (notification.hasData()) {
      fcm.put("data", notification.getData().getData());
    }

    publishRequest.put("fcm", fcm);

    beamsClient.publishToInterests(interests, publishRequest);
  }
}
