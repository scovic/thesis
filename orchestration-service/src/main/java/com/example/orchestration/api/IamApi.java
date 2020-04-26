package com.example.orchestration.api;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.iamservice.DeleteUserDto;
import com.example.orchestration.dto.iamservice.LoginRequestDto;
import com.example.orchestration.dto.iamservice.UserDto;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.TransactionStatus;
import com.example.orchestration.serviceproxy.IamServiceProxy;
import io.nats.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/iam")
public class IamApi {
  private IamServiceProxy iamServiceProxy;
  private Connection nats;

  @Autowired
  public IamApi(IamServiceProxy iamServiceProxy, Connection c) {
    this.iamServiceProxy = iamServiceProxy;
    this.nats = c;

//    Dispatcher d = nats.createDispatcher(message -> {
//      CreateUserReplyDto dto = new CreateUserReplyDto();
//      dto.setId(5);
//
//      ReplyMessage<CreateUserReplyDto> rm = new ReplyMessage<>(dto);
//      rm.setTransactionStatus(TransactionStatus.FAILURE);
//      rm.setData(dto);
//
//      String json = JsonUtil.toJson(rm);
//
//      nats.publish(message.getReplyTo(), json.toString().getBytes());
//    });
//
//    d.subscribe("iam-service-create");
  }

  @PostMapping
  public DeferredResult<ResponseEntity<?>> createUser(@RequestBody UserDto userDto) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    iamServiceProxy.createUser(new CommandMessage<UserDto>(userDto))
        .subscribe(replyMessage -> {
          if (replyMessage.getTransactionStatus() == TransactionStatus.FAILURE) {
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
                )
            );
          }
        });

    return result;
  }

  @PostMapping("/login")
  public DeferredResult<ResponseEntity<?>> login(@RequestBody LoginRequestDto loginRequestDto) {
    CommandMessage<LoginRequestDto> cm = new CommandMessage<>(loginRequestDto);

    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
    this.iamServiceProxy.login(new CommandMessage<>(loginRequestDto))
        .subscribe(replyMessage -> {
              if (replyMessage.getTransactionStatus() == TransactionStatus.FAILURE) {
                result.setErrorResult(
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Bad Credentials"
                    )
                );
              } else {
                result.setResult(
                    new ResponseEntity<>(
                        replyMessage.getData(),
                        HttpStatus.ACCEPTED
                    )
                );
              }
            },
            throwable -> {
              result.setErrorResult(new ResponseStatusException(
                  HttpStatus.INTERNAL_SERVER_ERROR,
                  throwable.getMessage(),
                  throwable
              ));
            });

    return result;
  }

  @PostMapping(value = "/authorize", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> authorize(@RequestHeader("Authorization") String token, String email) {
    AuthorizeDto dto = new AuthorizeDto();
    dto.setEmail(email);
    dto.setToken(token.substring(7));

    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    this.iamServiceProxy.authorize(new CommandMessage<AuthorizeDto>(dto))
        .subscribe(replyMessage -> {
              if (replyMessage.getTransactionStatus() == TransactionStatus.FAILURE) {
                result.setErrorResult(
                    new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Forbidden"
                    )
                );
              } else {
                result.setResult(
                    new ResponseEntity<>(
                        replyMessage.getData(),
                        HttpStatus.ACCEPTED
                    )
                );
              }
            },
            throwable -> {
              result.setErrorResult(new ResponseStatusException(
                  HttpStatus.INTERNAL_SERVER_ERROR,
                  throwable.getMessage(),
                  throwable
              ));
            });

    return result;
  }

  @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> deleteUser(
      @PathVariable("id") int id,
      @RequestHeader("Authorization") String token,
      String email
    ) {
    DeleteUserDto dto = new DeleteUserDto();
    dto.setId(id);

    AuthorizeDto authorizeDto = new AuthorizeDto();
    authorizeDto.setToken(token.substring(7));
    authorizeDto.setEmail(email);

    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    this.iamServiceProxy.authorize(new CommandMessage<AuthorizeDto>(authorizeDto))
        .switchMap(replyMessage -> {
          if (replyMessage.getTransactionStatus() == TransactionStatus.FAILURE) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Forbidden"
            );
          }

          return this.iamServiceProxy.deleteUser(new CommandMessage<DeleteUserDto>(dto));
        })
        .subscribe(replyMessage -> {
              if (replyMessage.getTransactionStatus() == TransactionStatus.FAILURE) {
                result.setErrorResult(
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Something went wrong"
                    )
                );
              } else {
                result.setErrorResult(
                    result.setResult(
                        new ResponseEntity<>(
                            replyMessage.getData(),
                            HttpStatus.ACCEPTED
                        )
                    )
                );
              }
            },
            throwable -> {
              result.setErrorResult(throwable);
            });

    return result;
  }
}
