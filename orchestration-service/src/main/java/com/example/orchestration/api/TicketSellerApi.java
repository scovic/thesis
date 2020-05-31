package com.example.orchestration.api;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.ticketsellerservice.CancelPurchaseDtoReq;
import com.example.orchestration.dto.ticketsellerservice.GetUserTicketsReqDto;
import com.example.orchestration.dto.ticketsellerservice.PurchaseTicketsReqDto;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.TicketSellerProxy;
import io.nats.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketSellerApi {
  @Autowired
  private TicketSellerProxy ticketSellerProxy;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  @Autowired
  private Connection connection;

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> purchaseTickets(
      @RequestHeader("Authorization") String token,
      String email,
      PurchaseTicketsReqDto dto) {
    AuthorizeDto authorizeDto = new AuthorizeDto();
    authorizeDto.setToken(token.substring(7));
    authorizeDto.setEmail(email);

    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    this.iamServiceProxy.authorize(new CommandMessage<>(authorizeDto))
        .switchMap(replyMessage -> {
          if (!replyMessage.isSuccess()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Forbidden"
            );
          }

          return this.ticketSellerProxy.purchaseTickets(new CommandMessage<>(dto));
        })
        .subscribe(replyMessage -> {
              if (!replyMessage.isSuccess()) {
                result.setErrorResult(
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Something went wrong"
                    ));
              } else {
                result.setResult(new ResponseEntity<>(
                    replyMessage.getData(),
                    HttpStatus.ACCEPTED
                ));
              }
            },
            throwable -> {
              result.setErrorResult(throwable);
            });

    return result;
  }

  @GetMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> getUserTickets(
      @PathVariable("id") int userId,
      @RequestHeader("Authorization") String token,
      @RequestParam("email") String email
  ) {
    AuthorizeDto authorizeDto = new AuthorizeDto();
    authorizeDto.setToken(token.substring(7));
    authorizeDto.setEmail(email);

    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    this.iamServiceProxy.authorize(new CommandMessage<>(authorizeDto))
        .switchMap(replyMessage -> {
          if (!replyMessage.isSuccess()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Forbidden"
            );
          }

          return this.ticketSellerProxy.getUserTickets(
              new CommandMessage<>(new GetUserTicketsReqDto(userId))
          );
        })
        .subscribe(replyMessage -> {
              if (!replyMessage.isSuccess()) {
                result.setErrorResult(
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Something went wrong"
                    )
                );
              } else {
                result.setResult(
                    new ResponseEntity<>(
                        replyMessage.getData(),
                        HttpStatus.ACCEPTED
                    ));
              }
            },
            throwable -> {
              result.setErrorResult(throwable);
            });

    return result;
  }

  @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> cancelPurchase(
      @PathVariable("id") int id,
      @RequestHeader("Authorization") String token,
      String email
  ) {
    AuthorizeDto authorizeDto = new AuthorizeDto();
    authorizeDto.setToken(token.substring(7));
    authorizeDto.setEmail(email);

    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    this.iamServiceProxy.authorize(new CommandMessage<AuthorizeDto>(authorizeDto))
        .switchMap(replyMessage -> {
          if (!replyMessage.isSuccess()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Forbidden"
            );
          }

          List<Integer> ids = new ArrayList<>();
          ids.add(id);
          return this.ticketSellerProxy.cancelPurchase(new CommandMessage<>(new CancelPurchaseDtoReq(ids)));
        })
        .subscribe(replyMessage -> {
              if (!replyMessage.isSuccess()) {
                result.setErrorResult(
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Something went wrong"
                    )
                );
              } else {
                result.setResult(
                    new ResponseEntity<>(
                        replyMessage.getData(),
                        HttpStatus.ACCEPTED
                    ));
              }
            },
            throwable -> {
              result.setErrorResult(throwable);
            });

    return result;
  }
}
