package com.example.orchestration.saga.ticketsellersagas;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.ticketsellerservice.CancelPurchaseDtoReq;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.TicketSellerProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CancelPurchaseSaga {
  private Step step;

  @Autowired
  private TicketSellerProxy ticketSellerProxy;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(String token, int ticketId) throws Exception {
    AuthorizeDto authorizeDto = new AuthorizeDto(token);

    List<Integer> ids = new ArrayList<>();
    ids.add(ticketId);

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            authorizeDto
        )
        .addStep(
            commandMessage -> this.ticketSellerProxy.cancelPurchase(commandMessage),
            new CancelPurchaseDtoReq(ids)
        )
        .build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
