package com.example.orchestration.dto.ticketsellerservice;

import java.util.List;

public class GetUserTicketsRespDto {
  private List<PurchaseDetailsDto> list;

  public GetUserTicketsRespDto() {
  }

  public GetUserTicketsRespDto(List<PurchaseDetailsDto> list) {
    this.list = list;
  }

  public List<PurchaseDetailsDto> getList() {
    return list;
  }

  public void setList(List<PurchaseDetailsDto> list) {
    this.list = list;
  }
}
