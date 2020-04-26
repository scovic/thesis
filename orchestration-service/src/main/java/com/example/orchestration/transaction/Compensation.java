package com.example.orchestration.transaction;

import com.example.orchestration.messages.CommandMessage;

public interface Compensation<T> {
  void execute(CommandMessage<T> compensationData);
}
