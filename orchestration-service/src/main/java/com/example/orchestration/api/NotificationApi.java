package com.example.orchestration.api;

import com.example.orchestration.dto.notificationservice.NotificationDto;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.saga.NotificationServiceSagaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/notifications")
public class NotificationApi {
  @Autowired
  private NotificationServiceSagaManager notificationServiceSagaManager;

  @PostMapping("/info")
  public DeferredResult<ResponseEntity<?>> sendInfoNotification(@RequestHeader("Authorization") String token, @RequestBody NotificationDto notificationDto) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.notificationServiceSagaManager
          .getMakeInfoNotificationSaga()
          .initSaga(token.substring(7), notificationDto);

      this.notificationServiceSagaManager
          .getMakeInfoNotificationSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @PostMapping("/warning")
  public DeferredResult<ResponseEntity<?>> sendWarningNotification(@RequestHeader("Authorization") String token, @RequestBody NotificationDto notificationDto) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.notificationServiceSagaManager
          .getMakeWarningNotificationSaga()
          .initSaga(token.substring(7), notificationDto);

      this.notificationServiceSagaManager
          .getMakeWarningNotificationSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }
}
