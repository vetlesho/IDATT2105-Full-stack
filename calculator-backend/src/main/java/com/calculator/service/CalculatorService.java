package com.calculator.service;

import com.calculator.model.CalculationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

  private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

  public double calculate(CalculationRequest request) {
    double num1 = request.getNum1();
    double num2 = request.getNum2();
    String operator = request.getOperator();

    logger.info("Performing calculation: {} {} {}", num1, operator, num2);

    return switch (operator) {
      case "+" -> num1 + num2;
      case "-" -> num1 - num2;
      case "*" -> num1 * num2;
      case "/" -> (num2 != 0) ? num1 / num2 : Double.NaN;
      default -> throw new IllegalArgumentException("Invalid operator");
    };
  }
}
