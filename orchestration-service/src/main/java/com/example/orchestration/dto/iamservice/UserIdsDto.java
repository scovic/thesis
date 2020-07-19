package com.example.orchestration.dto.iamservice;

import java.util.List;

public class UserIdsDto {
  List<Integer> userIds;

  public UserIdsDto(List<Integer> userIds) {
    this.userIds = userIds;
  }

  public List<Integer> getUserIds() {
    return userIds;
  }

  public void setUserIds(List<Integer> userIds) {
    this.userIds = userIds;
  }
}
