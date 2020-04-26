package com.example.orchestration.saga;

import com.example.orchestration.dto.iamservice.CreateUserReplyDto;
import com.example.orchestration.dto.iamservice.DeleteUserDto;
import com.example.orchestration.dto.iamservice.UserDto;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.serviceproxy.IamServiceProxy;
import com.example.orchestration.transaction.Compensation;
import com.example.orchestration.transaction.Step;
import com.example.orchestration.transaction.Transaction;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateUserSaga {
  @Autowired
  private IamServiceProxy iamServiceProxy;

  public CreateUserSaga() {
    new Saga.SagaBuilder()
        .addStep(new Step(
            new Transaction<CreateUserReplyDto, UserDto>() {
              @Override
              public Observable<ReplyMessage<CreateUserReplyDto>> execute(CommandMessage<UserDto> k) {
                return iamServiceProxy.createUser(k);
              }
            },
            new Compensation<DeleteUserDto>() {
              @Override
              public void execute(CommandMessage<DeleteUserDto> compensationData) {
                iamServiceProxy.deleteUser(compensationData);
              }
            }
        ));
  }

}
