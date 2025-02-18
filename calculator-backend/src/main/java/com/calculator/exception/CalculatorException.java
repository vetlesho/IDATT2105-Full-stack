package com.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CalculatorException extends RuntimeException {
  public CalculatorException(String message) {
    super(message);
  }
}