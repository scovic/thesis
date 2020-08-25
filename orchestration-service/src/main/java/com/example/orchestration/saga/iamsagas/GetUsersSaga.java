package com.example.orchestration.saga.iamsagas;

import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.iamservice.UserIdsDto;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetUsersSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  public void initSaga(String token, int id) throws Exception {
    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> this.iamServiceProxy.authorize(commandMessage),
            new AuthorizeDto(token)
        )
        .addStep(
            commandMessage -> this.iamServiceProxy.getUsers(commandMessage),
            new UserIdsDto(prepareData(id))
        ).build();
  }

  public Observable<?> executeSaga() {
    return this.step.executeTransaction();
  }

  private List<Integer> prepareData(int id) {
    List<Integer> data = new ArrayList<>();
    data.add(id);
    return data;
  }
}
