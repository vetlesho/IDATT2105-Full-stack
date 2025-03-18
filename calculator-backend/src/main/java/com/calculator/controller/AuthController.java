package com.calculator.controller;

import com.calculator.exception.AuthenticationException;
import com.calculator.model.User;
import com.calculator.security.JwtTokenUtil;
import com.calculator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user) {
    logger.info("Registration attempt for user: {}", user.getUsername());
    try {
      User registeredUser = userService.register(user.getUsername(), user.getPassword());

      Map<String, Object> response = new HashMap<>();
      response.put("username", registeredUser.getUsername());
      response.put("message", "User registered successfully");

      logger.info("User registered successfully: {}", user.getUsername());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      logger.error("Registration failed for user {}: {}", user.getUsername(), e.getMessage());
      throw e;
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    logger.info("Login attempt for user: {}", user.getUsername());
    try {
      // Authenticate using Spring Security
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
      );

      // Manage session (ensure only one user at a time)
      userService.login(user.getUsername(), user.getPassword());

      // Generate JWT token
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String token = jwtTokenUtil.generateToken(userDetails);

      Map<String, Object> response = new HashMap<>();
      response.put("token", token);
      response.put("username", userDetails.getUsername());
      response.put("expiresIn", 300); // 5 minutes in seconds

      logger.info("Login successful for user: {}", user.getUsername());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      logger.error("Authentication failed for user {}: {}", user.getUsername(), e.getMessage());
      throw e;
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
    logger.info("Logout attempt received");

    try {
      // Extract username from JWT token
      String token = authHeader.substring(7); // Remove "Bearer " prefix
      String username = jwtTokenUtil.extractUsername(token);

      // Handle session logout
      userService.logout(username);

      logger.info("User '{}' logged out successfully", username);
      return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    } catch (Exception e) {
      logger.error("Logout failed: {}", e.getMessage());
      return ResponseEntity.badRequest().body(Map.of("error", "Logout failed"));
    }
  }
}