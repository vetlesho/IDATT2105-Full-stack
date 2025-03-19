/*package com.calculator.security;

import com.calculator.model.User;
import com.calculator.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordUpdater {
  //@Autowired
  private UserRepository userRepository;
  //
  private PasswordEncoder passwordEncoder;

  //@PostConstruct
  public void updateAllPasswords() {
    List<User> users = userRepository.findAll();
    for (User user : users) {
      // Check if password is not BCrypt encoded
      if (!user.getPassword().startsWith("$2")) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
      }
    }
  }
}*/