package com.calculator.controller;

import com.calculator.model.User;
import com.calculator.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user) {
    Map<String, Object> response = authService.registerUser(user.getUsername(), user.getPassword());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    Map<String, Object> response = authService.loginUser(user.getUsername(), user.getPassword());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
    Map<String, Object> response = authService.logoutUser(authHeader);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
    Map<String, Object> response = authService.refreshToken(authHeader);
    return ResponseEntity.ok(response);
  }
}