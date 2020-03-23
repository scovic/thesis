package com.stefan.ticketseller.exception;

public class NotEnoughTicketsException extends Exception {
  public NotEnoughTicketsException () {
    super("Not enough tickets to fulfil purchase");
  }
}
