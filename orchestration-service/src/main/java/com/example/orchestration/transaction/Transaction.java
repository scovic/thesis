package com.example.orchestration.transaction;

import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import io.reactivex.rxjava3.core.Observable;

public interface Transaction<N, M> {
  Observable<ReplyMessage<M>> execute(CommandMessage<N> commandMessage) throws Exception;
}
