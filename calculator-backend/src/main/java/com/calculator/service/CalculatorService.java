package com.calculator.service;

import com.calculator.model.Calculation;
import com.calculator.model.CalculationRequest;
import com.calculator.exception.CalculatorException;
import com.calculator.model.User;
import com.calculator.repository.CalculationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CalculatorService {
  private final CalculationRepository calculationRepository;
  private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

  @Autowired
  public CalculatorService(CalculationRepository calculationRepository) {
    this.calculationRepository = calculationRepository;
  }

  public Page<Calculation> getCalculationHistory(User user, int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timeStamp"));
    return calculationRepository.findByUserOrderByTimeStamp(user, pageRequest);
  }

  private void saveCalculation(String expression, double result, User user) {
    Calculation calculation = new Calculation();
    calculation.setExpression(expression);
    calculation.setResult(String.valueOf(result));
    calculation.setTimeStamp(LocalDateTime.now());
    calculation.setUser(user);
    calculationRepository.save(calculation);
    logger.info("Saved calculation for user: {}", user.getUsername());
  }

  public double calculate(CalculationRequest request, User user) {
    double result = calculateExpression(request.getExpression());
    saveCalculation(request.getExpression(), result, user);
    return result;
  }

  private double calculateExpression(String expression) {
    String trimmedExpression = expression.trim();
    logger.info("Calculating expression: {}", trimmedExpression);

    if (trimmedExpression.isEmpty()) {
      throw new CalculatorException("Expression is empty");
    }

    try {
      return Double.parseDouble(trimmedExpression);
    } catch (NumberFormatException e) {
      List<String> tokens = tokenizeExpression(trimmedExpression);
      logger.info("Tokenized expression: {}", tokens);
      return evaluateExpression(tokens);
    }
  }


  /*public double calculate(CalculationRequest request, User user) {
    String expression = request.getExpression().trim();
    logger.info("Calculating expression: {}", expression);

    if (expression.isEmpty()) {
      throw new CalculatorException("Expression is empty");
    }

    try {
      // Handle single number case
      return Double.parseDouble(expression);
    } catch (NumberFormatException e) {
      // Continue with expression parsing
    }

    // Split expression into numbers and operators while preserving negative signs
    List<String> tokens = tokenizeExpression(expression);
    logger.info("Tokenized expression: {}", tokens);
    /*if (!tokens.isEmpty() && isOperator(tokens.get(tokens.size() - 1).charAt(0))) {
      logger.error("Expression ends with an operator");
      throw new CalculatorException("Expression cannot end with an operator");
    }

    return evaluateExpression(tokens);
  }*/

  private List<String> tokenizeExpression(String expression) {
    List<String> tokens = new ArrayList<>();
    StringBuilder currentNumber = new StringBuilder();

    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);

      if (isOperator(c)) {
        // Handle negative numbers in three cases:
        // 1. At the start of expression
        // 2. After another operator
        // 3. After an opening parenthesis (if we add support for them later)
        boolean isNegativeNumber = c == '-' && (i == 0 || isOperator(expression.charAt(i - 1)) || expression.charAt(i - 1) == '/');

        if (isNegativeNumber) {
          currentNumber.append(c);
        } else {
          if (!currentNumber.isEmpty()) {
            tokens.add(currentNumber.toString());
            currentNumber.setLength(0);
          }
          tokens.add(String.valueOf(c));
        }
      } else {
        currentNumber.append(c);
      }
    }

    if (!currentNumber.isEmpty()) {
      tokens.add(currentNumber.toString());
    }

    // Validate the final token list
    if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).matches("[+\\-*/]")) {
      throw new CalculatorException("Expression cannot end with an operator");
    }

    return tokens;
  }

  private double evaluateExpression(List<String> tokens) {
    // First handle multiplication and division
    for (int i = 1; i < tokens.size() - 1; i += 2) {
      String operator = tokens.get(i);
      if (operator.equals("*") || operator.equals("/")) {
        double num1 = Double.parseDouble(tokens.get(i - 1));
        double num2 = Double.parseDouble(tokens.get(i + 1));
        double result;

        if (operator.equals("*")) {
          result = num1 * num2;
        } else {
          if (num2 == 0) {
            logger.error("Division by zero attempted");
            throw new CalculatorException("Cannot divide by zero");
          }
          result = num1 / num2;
        }

        tokens.set(i - 1, String.valueOf(result));
        tokens.remove(i);
        tokens.remove(i);
        i -= 2;
      }
    }

    // Then handle addition and subtraction
    double result = Double.parseDouble(tokens.get(0));
    for (int i = 1; i < tokens.size() - 1; i += 2) {
      String operator = tokens.get(i);
      double num2 = Double.parseDouble(tokens.get(i + 1));

      if (operator.equals("+")) {
        result += num2;
      } else if (operator.equals("-")) {
        result -= num2;
      }
    }

    logger.info("Calculation result: {}", result);
    return result;
  }

  private boolean isOperator(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/';
  }
}