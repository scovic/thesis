package com.example.orchestration.transaction;

import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import io.reactivex.rxjava3.core.Observable;

public interface Compensation<N> {
  void execute(N compensationCommandMessage);
}
