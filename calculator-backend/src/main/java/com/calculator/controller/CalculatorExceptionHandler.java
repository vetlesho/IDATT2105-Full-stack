package com.calculator.controller;

import com.calculator.model.CalculatorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CalculatorExceptionHandler {

  @ExceptionHandler(CalculatorException.class)
  public ResponseEntity<Map<String, String>> handleCalculatorException(CalculatorException e) {
    Map<String, String> response = new HashMap<>();
    response.put("error", e.getMessage());
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Calculation error: " + e.getMessage());
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
  }
}