package com.example.orchestration.api;

import com.example.orchestration.dto.ticketsellerservice.*;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.saga.TicketSellerServiceSagaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/tickets")
public class TicketSellerApi {
  @Autowired
  private TicketSellerServiceSagaManager ticketSellerServiceSagaManager;

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> purchaseTickets(
      @RequestHeader("Authorization") String token,
      PurchaseTicketsReqDto dto) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.ticketSellerServiceSagaManager
          .getPurchaseTicketsSaga()
          .initSaga(token.substring(7), dto);

      this.ticketSellerServiceSagaManager
          .getPurchaseTicketsSaga().executeSaga()
          .subscribe(
              replyMessage -> {
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

  @GetMapping(path = "/{id}")
  public DeferredResult<ResponseEntity<?>> getUserTickets(
      @PathVariable("id") int userId,
      @RequestHeader("Authorization") String token
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.ticketSellerServiceSagaManager
          .getGetUserTicketsSaga()
          .initSaga(token.substring(7), userId);

      this.ticketSellerServiceSagaManager
          .getGetUserTicketsSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;

                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      )
                  );
                } else {
                  result.setResult(
                      new ResponseEntity<>(
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
          )
      );
    }

    return result;
  }

  @DeleteMapping(path = "/{id}")
  public DeferredResult<ResponseEntity<?>> cancelPurchase(
      @PathVariable("id") int id,
      @RequestHeader("Authorization") String token
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.ticketSellerServiceSagaManager
          .getCancelPurchaseSaga()
          .initSaga(token.substring(7), id);

      this.ticketSellerServiceSagaManager
          .getCancelPurchaseSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;

                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      )
                  );
                } else {
                  result.setResult(
                      new ResponseEntity<>(
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
          )
      );
    }

    return result;
  }
}
