package com.example.orchestration.transaction;

import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import io.reactivex.rxjava3.core.Observable;

public class Step {
  private Transaction transaction;
  private Compensation compensation;

  public Step(Transaction transaction) {
    this.transaction = transaction;
  }

  public Step(Transaction transaction, Compensation compensation) {
    this.transaction = transaction;
    this.compensation = compensation;
  }

  public <T, K> Observable<ReplyMessage<K>> executeTransaction(CommandMessage<T> transactionData) {
    return this.transaction.execute(transactionData);
  }

//  public void executeCompensation() {
//    if (this.compensation != null) {
//      this.compensation.execute(compensationData);
//    }
//  }

  public Transaction getTransaction() {
    return transaction;
  }

//  public void setTransaction(Transaction<T, K> transaction) {
//    this.transaction = transaction;
//  }

//  public Compensation<T> getCompensation() {
//    return compensation;
//  }

//  public void setCompensation(Compensation<T> compensation) {
//    this.compensation = compensation;
//  }

//  public CommandMessage<?> getCompensationData() {
//    return compensationData;
//  }

//  public void setCompensationData(CommandMessage<T> compensationData) {
//    this.compensationData = compensationData;
//  }
}
