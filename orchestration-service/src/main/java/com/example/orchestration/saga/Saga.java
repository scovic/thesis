package com.example.orchestration.saga;

import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.messages.TransactionStatus;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;
import java.util.Stack;

public class Saga {
  private List<Step> steps;
  private Stack<Step> finishedSteps;
  private int currentStep = 0;

  private Saga(List<Step> steps) {
    this.steps = steps;
  }

//  public  Observable<ReplyMessage<?>> executeTransactions(Object transactionData) {
//    if (currentStep < steps.size()) {
//      CommandMessage<?> cm = new CommandMessage<>();
////      cm.setData(transactionData);
//
//      return this.steps.get(currentStep).executeTransaction(transactionData)
//          .map(replyMessage -> {
//            if (replyMessage.getTransactionStatus() == TransactionStatus.FAILURE) {
//              this.executeCompensation();
//              return null;
//            } else {
////              if (this.steps.get(currentStep).getCompensation() != null) {
////                this.finishedSteps.push(this.steps.get(currentStep));
//              }
//              currentStep++;
//
////              CommandMessage<?> cm = new CommandMessage();
////              cm.setData(replyMessage.getData());
////              return this.executeTransactions(cm);
//            }
//          });
//    }
//
//    return null;
//  }

  public void executeCompensation() {
    while (!finishedSteps.empty()) {
      Step s = finishedSteps.pop();
//      s.executeCompensation();
    }
  }

  public static class SagaBuilder {
    private List<Step> steps;

    public SagaBuilder() {
    }

    public SagaBuilder addStep(Step step) {
      this.steps.add(step);
      return this;
    }

    public Saga build() {
      return new Saga(this.steps);
    }
  }
}
