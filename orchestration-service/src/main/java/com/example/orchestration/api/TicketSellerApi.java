package com.example.orchestration.api;

import com.example.orchestration.dto.ticketsellerservice.*;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.TicketSellerProxy;
import com.example.orchestration.saga.CancelPurchaseSaga;
import com.example.orchestration.saga.GetUserTicketsSaga;
import com.example.orchestration.saga.PurchaseTicketsSaga;
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
  private TicketSellerProxy ticketSellerProxy;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  @Autowired
  private PurchaseTicketsSaga purchaseTicketsSaga;

  @Autowired
  private GetUserTicketsSaga getUserTicketsSaga;

  @Autowired
  private CancelPurchaseSaga cancelPurchaseSaga;

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> purchaseTickets(
      @RequestHeader("Authorization") String token,
      String email,
      PurchaseTicketsReqDto dto) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      purchaseTicketsSaga.initSaga(token.substring(7), email, dto);

      purchaseTicketsSaga.executeSaga()
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

  @GetMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> getUserTickets(
      @PathVariable("id") int userId,
      @RequestHeader("Authorization") String token,
      @RequestParam("email") String email
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.getUserTicketsSaga.initSaga(token.substring(7), email, userId);

      this.getUserTicketsSaga.executeSaga()
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

  @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> cancelPurchase(
      @PathVariable("id") int id,
      @RequestHeader("Authorization") String token,
      String email
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      cancelPurchaseSaga.initSaga(token.substring(7), email, id);
      cancelPurchaseSaga.executeSaga()
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
