package com.example.orchestration.api;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.iamservice.DeleteUserDto;
import com.example.orchestration.dto.iamservice.LoginRequestDto;
import com.example.orchestration.dto.iamservice.UserDto;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.messages.TransactionStatus;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.saga.AuthorizeSaga;
import com.example.orchestration.saga.CreateUserSaga;
import com.example.orchestration.saga.DeleteUserSaga;
import com.example.orchestration.saga.LoginSaga;
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
  @Autowired
  private IamServiceProxy iamServiceProxy;

  @Autowired
  private CreateUserSaga createUserSaga;

  @Autowired
  private LoginSaga loginSaga;

  @Autowired
  private AuthorizeSaga authorizeSaga;

  @Autowired
  private DeleteUserSaga deleteUserSaga;

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> createUser(UserDto userDto) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.createUserSaga.initSaga(userDto);

      createUserSaga.executeSaga()
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

  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> login(LoginRequestDto loginRequestDto) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.loginSaga.initSaga(loginRequestDto);

      this.loginSaga.executeSaga()
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

  @PostMapping(value = "/authorize", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> authorize(@RequestHeader("Authorization") String token, String email) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      authorizeSaga.initSata(token.substring(7), email);

      authorizeSaga.executeSaga()
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

  @DeleteMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> deleteUser(
      @PathVariable("id") int id,
      @RequestHeader("Authorization") String token,
      String email
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.deleteUserSaga.initSaga(token.substring(7), email, id);

      this.deleteUserSaga.executeSaga()
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
