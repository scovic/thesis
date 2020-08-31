package com.example.orchestration.saga;

import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.saga.ticketsellersagas.CancelPurchaseSaga;
import com.example.orchestration.saga.ticketsellersagas.GetUserTicketsSaga;
import com.example.orchestration.saga.ticketsellersagas.PurchaseTicketsSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketSellerServiceSagaManager {
  @Autowired
  private IamServiceProxy iamServiceProxy;

  @Autowired
  private PurchaseTicketsSaga purchaseTicketsSaga;

  @Autowired
  private GetUserTicketsSaga getUserTicketsSaga;

  @Autowired
  private CancelPurchaseSaga cancelPurchaseSaga;

  public IamServiceProxy getIamServiceProxy() {
    return iamServiceProxy;
  }

  public PurchaseTicketsSaga getPurchaseTicketsSaga() {
    return purchaseTicketsSaga;
  }

  public GetUserTicketsSaga getGetUserTicketsSaga() {
    return getUserTicketsSaga;
  }

  public CancelPurchaseSaga getCancelPurchaseSaga() {
    return cancelPurchaseSaga;
  }
}
