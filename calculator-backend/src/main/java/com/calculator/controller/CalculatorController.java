package com.calculator.controller;

import com.calculator.model.CalculationRequest;
import com.calculator.model.CalculationResult;
import com.calculator.model.User;
import com.calculator.service.CalculatorService;
import com.calculator.service.UserService;
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
  private final UserService userService;

  @Autowired
  public CalculatorController(CalculatorService calculatorService, UserService userService) {
    this.calculatorService = calculatorService;
    this.userService = userService;
  }

  // try catch som er i service klassen burde heller være i controlleren
  @PostMapping("/calculate") // Lytter for POST-requests på localhost:xxxx/api/calculator/calculate
  public ResponseEntity<CalculationResult> calculate(
          @RequestBody CalculationRequest request,
          @RequestHeader("username") String username) {
    logger.info("Received calculation request from user {}: {}", username, request.getExpression());

    User user = userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    double result = calculatorService.calculate(request, user);
    return ResponseEntity.ok(new CalculationResult(result));
  }

  @GetMapping("/history")
  public ResponseEntity<?> getHistory(
          @RequestHeader("username") String username,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    User user = userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return ResponseEntity.ok(calculatorService.getCalculationHistory(user, page, size));
  }
}