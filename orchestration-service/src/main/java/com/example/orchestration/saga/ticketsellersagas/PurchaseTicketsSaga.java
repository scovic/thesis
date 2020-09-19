package com.example.orchestration.saga.ticketsellersagas;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.ticketsellerservice.CancelPurchaseDtoReq;
import com.example.orchestration.dto.ticketsellerservice.PurchaseDetailsDto;
import com.example.orchestration.dto.ticketsellerservice.PurchaseTicketsReqDto;
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
public class PurchaseTicketsSaga {
  private Step step;

  @Autowired
  private TicketSellerProxy ticketSellerProxy;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(String token, PurchaseTicketsReqDto dto) throws Exception {
    AuthorizeDto authorizeDto = new AuthorizeDto(token);

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            authorizeDto
        )
        .addStep(
            commandMessage -> this.ticketSellerProxy.purchaseTickets(commandMessage),
            compensationCommandMessage -> {
              List<Integer> ids = new ArrayList<>();

              for (PurchaseDetailsDto purchaseDetailsDto : compensationCommandMessage.getList()) {
                ids.add(purchaseDetailsDto.getId());
              }

              this.ticketSellerProxy.cancelPurchase(new CommandMessage<>(new CancelPurchaseDtoReq(ids)))
                  .subscribe(); 
            },
            dto
        ).build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
