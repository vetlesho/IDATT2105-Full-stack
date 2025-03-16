package com.calculator.controller;

import com.calculator.model.Calculation;
import com.calculator.model.CalculationRequest;
import com.calculator.model.CalculationResult;
import com.calculator.service.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "http://localhost:5173")
public class CalculatorController {
  private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
  private final CalculatorService calculatorService;

  @Autowired
  public CalculatorController(CalculatorService calculatorService) {
    this.calculatorService = calculatorService;
  }

  @PostMapping("/calculate") // Lytter for POST-requests på localhost:xxxx/api/calculator/calculate
  public ResponseEntity<CalculationResult> calculate(
          @RequestBody CalculationRequest request,
          @RequestHeader("username") String username) {
    logger.info("Received calculation request from user {}: {}", username, request.getExpression());

    double result = calculatorService.calculateForUser(request, username);
    return ResponseEntity.ok(new CalculationResult(result));
  }

  @GetMapping("/history")
  public ResponseEntity<Page<Calculation>> getHistory(
          @RequestHeader("username") String username,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "5") int size) {
    Page<Calculation> history = calculatorService.getCalculationHistoryForUser(username, page, size);
  return ResponseEntity.ok(history);  }
}