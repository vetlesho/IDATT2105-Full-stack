package com.calculator.controller;

import com.calculator.model.CalculationRequest;
import com.calculator.model.CalculationResult;
import com.calculator.service.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  // try catch som er i service klassen burde heller være i controlleren
  @PostMapping("/calculate") // Lytter for POST-requests på localhost:xxxx/api/calculator/calculate
  public ResponseEntity<CalculationResult> calculate(@RequestBody CalculationRequest request) {
    logger.info("Received calculation request: {}", request.getExpression());
    double result = calculatorService.calculate(request);
    return ResponseEntity.ok(new CalculationResult(result));
  }
}