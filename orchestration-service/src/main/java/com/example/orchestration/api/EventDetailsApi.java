package com.example.orchestration.api;

import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.saga.EventDetailsServiceSagaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/event-details")
public class EventDetailsApi {
  @Autowired
  private EventDetailsServiceSagaManager eventDetailsServiceSagaManager;

  @GetMapping
  public DeferredResult<ResponseEntity<?>> getEventObjects(@RequestHeader("Authorization") String token) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();


    try {
      eventDetailsServiceSagaManager.getGetEventObjectsSaga().initSaga(token.substring(7));

      eventDetailsServiceSagaManager.getGetEventObjectsSaga()
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

  @GetMapping("/{stageId}")
  public DeferredResult<ResponseEntity<?>> getStagePerformers(
      @RequestHeader("Authorization") String token,
      @PathVariable("stageId") int stageId
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();


    try {
      eventDetailsServiceSagaManager.getGetStagePerformersSaga().initSaga(token.substring(7), stageId);

      eventDetailsServiceSagaManager.getGetStagePerformersSaga()
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
