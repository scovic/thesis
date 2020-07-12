package com.example.orchestration.saga;

import com.example.orchestration.saga.iamsagas.AuthorizeSaga;
import com.example.orchestration.saga.iamsagas.CreateUserSaga;
import com.example.orchestration.saga.iamsagas.DeleteUserSaga;
import com.example.orchestration.saga.iamsagas.LoginSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IamServiceSagasManager {
  @Autowired
  private CreateUserSaga createUserSaga;

  @Autowired
  private LoginSaga loginSaga;

  @Autowired
  private AuthorizeSaga authorizeSaga;

  @Autowired
  private DeleteUserSaga deleteUserSaga;

  public CreateUserSaga getCreateUserSaga() {
    return createUserSaga;
  }

  public LoginSaga getLoginSaga() {
    return loginSaga;
  }

  public AuthorizeSaga getAuthorizeSaga() {
    return authorizeSaga;
  }

  public DeleteUserSaga getDeleteUserSaga() {
    return deleteUserSaga;
  }
}
