package com.calculator.controller;

import com.calculator.model.CalculationRequest;
import com.calculator.model.CalculationResult;
import com.calculator.service.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "http://localhost:5173") // Allow Vue frontend to call API
public class CalculatorController {

  private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
  private final CalculatorService calculatorService;

  public CalculatorController(CalculatorService calculatorService) {
    this.calculatorService = calculatorService;
  }

  @PostMapping("/calculate")
  public ResponseEntity<CalculationResult> calculate(@RequestBody CalculationRequest request) {
    logger.info("Received calculation request: {} {} {}", request.getNum1(), request.getOperator(), request.getNum2());
    double result = calculatorService.calculate(request);
    return ResponseEntity.ok(new CalculationResult(result));
  }
}
