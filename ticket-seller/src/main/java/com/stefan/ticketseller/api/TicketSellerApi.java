package com.stefan.ticketseller.api;

import com.stefan.ticketseller.dto.PurchaseTicketsReqDto;
import com.stefan.ticketseller.dto.PurchaseTicketsRespDto;
import com.stefan.ticketseller.exception.PurchaseNotCompletedException;
import com.stefan.ticketseller.model.PurchaseDetails;
import com.stefan.ticketseller.service.Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ticket-seller")
public class TicketSellerApi {
  private final Service service;
  private final ModelMapper modelMapper;

  @Autowired
  public TicketSellerApi(@Qualifier("service") Service service, ModelMapper modelMapper) {
    this.service = service;
    this.modelMapper = modelMapper;
  }

  @PostMapping( consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<Boolean> purchaseTicket(PurchaseTicketsReqDto dto) {

    try {
      this.service.purchaseTicket(dto.getUserId(), dto.getQuantity());
      return new ResponseEntity<>(
          true,
          HttpStatus.OK
      );
    } catch (PurchaseNotCompletedException ex) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Purchase request couldn't be completed"
        );
    }
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Boolean> cancelPurchase(@PathVariable("id") int id) {
    try {
      this.service.cancelPurchase(id);

      return new ResponseEntity<>(
          true,
          HttpStatus.OK
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Purchase with provided id doesn't exist"
      );
    }
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<List<PurchaseTicketsRespDto>> getUserTickets(
      @PathVariable("id") int userId
  ) {
    return new ResponseEntity<>(
        convertToDtos(this.service.getUserTickets(userId)),
        HttpStatus.OK
    );
  }

  private List<PurchaseTicketsRespDto> convertToDtos(List<PurchaseDetails> purchaseDetails) {
    List<PurchaseTicketsRespDto> dtos = new ArrayList<>();

    for (int i = 0; i < purchaseDetails.size(); i++) {
      dtos.add(this.convertToRespDto(purchaseDetails.get(i)));
    }

    return dtos;
  }

  private PurchaseTicketsRespDto convertToRespDto(PurchaseDetails purchaseDetails) {
    return modelMapper.map(purchaseDetails, PurchaseTicketsRespDto.class);
  }

  private PurchaseDetails convertToModel(PurchaseTicketsReqDto dto) {
    return modelMapper.map(dto, PurchaseDetails.class);
  }


}
