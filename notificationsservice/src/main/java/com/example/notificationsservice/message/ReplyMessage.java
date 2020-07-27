package com.example.notificationsservice.message;

public class ReplyMessage<T> {
  private TransactionStatus transactionStatus;
  private T data;

  public ReplyMessage() {
    this.transactionStatus = TransactionStatus.SUCCESS;
  }

  public ReplyMessage(T data) {
    this.data = data;
    this.transactionStatus = TransactionStatus.SUCCESS;
  }

  public ReplyMessage(TransactionStatus transactionStatus, T data) {
    this.transactionStatus = transactionStatus;
    this.data = data;
  }

  public void setFailure() {
    this.transactionStatus = TransactionStatus.FAILURE;
  }

  public TransactionStatus getTransactionStatus() {
    return transactionStatus;
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
