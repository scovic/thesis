package com.example.orchestration.proxy;

import com.example.orchestration.MessagePublisher;
import com.example.orchestration.dto.ticketsellerservice.*;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.util.GenericTypeUtil;
import com.example.orchestration.util.JsonUtil;
import io.nats.client.Message;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Component
public class TicketSellerProxy {
  private String serviceTopic;
  private MessagePublisher messagePublisher;

  @Autowired
  public TicketSellerProxy(MessagePublisher messagePublisher) {
    this.serviceTopic = "ticket-seller-service-";
    this.messagePublisher = messagePublisher;
  }

  public Observable<ReplyMessage<PurchaseTicketsRespDto>> purchaseTickets(CommandMessage<PurchaseTicketsReqDto> commandMessage) {
    return this.messagePublisher.request(this.getTopic("purchase"), JsonUtil.toJson(commandMessage))
        .map(message -> this.<PurchaseTicketsRespDto>handleReply(message, PurchaseTicketsRespDto.class));
  }

  public Observable<ReplyMessage<Boolean>> cancelPurchase(CommandMessage<CancelPurchaseDtoReq> commandMessage) {
    return this.messagePublisher.request(this.getTopic("cancel"), JsonUtil.toJson(commandMessage))
        .map(message -> this.<Boolean>handleReply(message, Boolean.class));
  }


  public Observable<ReplyMessage<GetUserTicketsRespDto>> getUserTickets(CommandMessage<GetUserTicketsReqDto> commandMessage) {
    return this.messagePublisher.request(this.getTopic("get-tickets"), JsonUtil.toJson(commandMessage))
        .map(message -> this.<GetUserTicketsRespDto>handleReply(message, GetUserTicketsRespDto.class));
  }

  private <T> ReplyMessage<T> handleReply(Message msg, Class<?> dataType) {
    String json = new String(msg.getData(), StandardCharsets.UTF_8);
    Type replyMessageType = GenericTypeUtil.getType(ReplyMessage.class, dataType);

    return (ReplyMessage<T>) JsonUtil.fromJson(json, replyMessageType);
  }

  private String getTopic(String t) {
    return String.format("%s%s", this.serviceTopic, t);
  }
}
