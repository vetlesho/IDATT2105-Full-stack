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

  @GetMapping("/{username}")
  public ResponseEntity<?> getUserProfile(@PathVariable String username) {
    return ResponseEntity.ok(userService.getUserByUsername(username));
  }
}
