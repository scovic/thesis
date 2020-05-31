package com.stefan.ticketseller;

import com.stefan.ticketseller.messagehandler.CancelPurchaseMessageHandler;
import com.stefan.ticketseller.messagehandler.GetUserTicketsMessageHandler;
import com.stefan.ticketseller.messagehandler.PurchaseTicketsMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerManager {
  private final String topic;

  @Autowired
  public MessageHandlerManager(
      MessageSubscriber messageSubscriber,
      PurchaseTicketsMessageHandler purchaseTicketsMessageHandler,
      CancelPurchaseMessageHandler cancelPurchaseMessageHandler,
      GetUserTicketsMessageHandler getUserTicketsMessageHandler
  ) {
    this.topic = "ticket-seller-service-";

    messageSubscriber.subscribe(this.getTopic("purchase"), purchaseTicketsMessageHandler);
    messageSubscriber.subscribe(this.getTopic("cancel"), cancelPurchaseMessageHandler);
    messageSubscriber.subscribe(this.getTopic("get-tickets"), getUserTicketsMessageHandler);
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.topic, t);
  }
}
