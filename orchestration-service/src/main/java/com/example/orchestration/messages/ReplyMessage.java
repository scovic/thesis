package com.example.orchestration.messages;

public class ReplyMessage<T> {
  private TransactionStatus transactionStatus;
  private T data;

  public ReplyMessage(T data) {
    this.data = data;
  }

  public ReplyMessage(TransactionStatus transactionStatus, T data) {
    this.transactionStatus = transactionStatus;
    this.data = data;
  }

  public TransactionStatus getTransactionStatus() {
    return transactionStatus;
  }

  public boolean isSuccess () {
    return getTransactionStatus() == TransactionStatus.SUCCESS;
  }

  public void setTransactionStatus(TransactionStatus transactionStatus) {
    this.transactionStatus = transactionStatus;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
