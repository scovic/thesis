package com.example.notificationsservice.service;

import com.example.notificationsservice.model.Notification;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Service {
  void publishInfoNotification(Notification notification) throws IOException, InterruptedException, URISyntaxException;
  void publishWarningNotification(Notification notification) throws IOException, InterruptedException, URISyntaxException;
}
