package com.calculator.controller;

import com.calculator.model.User;
import com.calculator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody User user) {
    return ResponseEntity.ok(userService.register(user.getUsername(), user.getPassword()));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    return ResponseEntity.ok(userService.login(user.getUsername(), user.getPassword()));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("username") String username) {
    userService.logout(username);
    return ResponseEntity.ok().build();
  }
}
