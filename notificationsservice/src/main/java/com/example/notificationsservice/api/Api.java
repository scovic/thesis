package com.example.notificationsservice.api;

import com.example.notificationsservice.dto.NotificationDto;
import com.example.notificationsservice.model.Notification;
import com.example.notificationsservice.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/notifications")
public class Api {
  @Autowired
  private Service service;

  @PostMapping
  public void sendNotification(@RequestBody NotificationDto notificationDto) {
//    try {
//      if (notificationDto.getType().equals("info")) {
//        this.service.publishInfoNotification(new Notification(notificationDto));
//      } else {
//        this.service.publishWarningNotification(new Notification(notificationDto));
//      }
//    } catch (Exception ex) {
//      throw new ResponseStatusException(
//          HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex
//      );
//    }
  }
}
