package com.example.orchestration.messages;

public enum TransactionStatus {
  SUCCESS("success"),
  FAILURE("failure");

  private String label;

  TransactionStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
