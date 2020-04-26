//package com.example.orchestration;
//
//import com.google.gson.Gson;
//import io.nats.client.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class NatsConnection {
//  @Autowired
//  public NatsConnection(Connection nats) {
//    Dispatcher d = nats.createDispatcher(message -> {
//      String json = new String(message.getData(), StandardCharsets.UTF_8);
//      System.out.println(json);
//
//      Gson gson = new Gson();
//
////      CreateUserMessage c = gson.fromJson(json, CreateUserMessage.class);
//      SomeotherClass c = gson.fromJson(json, SomeotherClass.class);
//
//      nats.publish(message.getReplyTo(), c.toString().getBytes());
//    });
//
//    d.subscribe("sub");
//  }
//}
