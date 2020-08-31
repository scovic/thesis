package com.example.orchestration.saga;

import com.example.orchestration.saga.iamsagas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IamServiceSagaManager {
  @Autowired
  private CreateUserSaga createUserSaga;

  @Autowired
  private LoginSaga loginSaga;

  @Autowired
  private AuthorizeSaga authorizeSaga;

  @Autowired
  private DeleteUserSaga deleteUserSaga;

  @Autowired
  private GetUsersSaga getUsersSaga;

  @Autowired
  private UpdateUserSaga updateUserSaga;

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

  public GetUsersSaga getGetUsersSaga() {
    return getUsersSaga;
  }

  public UpdateUserSaga getUpdateUserSaga() {
    return updateUserSaga;
  }
}
