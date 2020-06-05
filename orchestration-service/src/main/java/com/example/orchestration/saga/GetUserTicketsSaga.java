package com.example.orchestration.saga;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.ticketsellerservice.GetUserTicketsReqDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.TicketSellerProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetUserTicketsSaga {
  private Step step;

  @Autowired
  private TicketSellerProxy ticketSellerProxy;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(String token, String email, int userId) throws Exception {
    AuthorizeDto authorizeDto = new AuthorizeDto(token, email);

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            authorizeDto
        )
        .addStep(
            commandMessage -> this.ticketSellerProxy.getUserTickets(commandMessage),
            new GetUserTicketsReqDto(userId)
        )
        .build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
