package com.example.orchestration.dto.ticketsellerservice;

import java.util.List;

public class CancelPurchaseDtoReq {
  private List<Integer> ids;

  public CancelPurchaseDtoReq(List<Integer> ids) {
    this.ids = ids;
  }

  public List<Integer> getIds() {
    return ids;
  }

  public void setIds(List<Integer> ids) {
    this.ids = ids;
  }
}
