package com.stefan.ticketseller.dto;

import java.util.List;

public class PurchaseTicketsRespDto {
  private List<PurchaseDetailsDto> list;

  public PurchaseTicketsRespDto() {
  }

  public PurchaseTicketsRespDto(List<PurchaseDetailsDto> list) {
    this.list = list;
  }

  public List<PurchaseDetailsDto> getList() {
    return list;
  }

  public void setList(List<PurchaseDetailsDto> list) {
    this.list = list;
  }
}
