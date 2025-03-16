package com.calculator.service;

import com.calculator.model.User;
import com.calculator.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final SessionService sessionService;

  @Autowired
  public UserService(UserRepository userRepository, SessionService sessionService) {
    this.userRepository = userRepository;
    this.sessionService = sessionService;
  }

  public User login(String username, String password) {
    logger.info("Login attempt for user: '{}'", username);
    User user = userRepository.findByUsername(username)
            .filter(u -> u.getPassword().equals(password))
            .orElseThrow(() -> {
              logger.warn("Login failed for user '{}'", username);
              return new RuntimeException("Invalid username or password");
            });

    if (!sessionService.login(username)) {
      throw new RuntimeException("Another user is already logged in");
    }

    logger.info("Login successful for user '{}'", username);
    return user;
  }

  public void logout(String username) {
    try {
      logger.info("Logout attempt for user '{}'", username);
      if (!sessionService.isUserLoggedIn(username)) {
        logger.warn("Cannot logout user '{}' - not logged in", username);
        throw new RuntimeException("User not logged in");
      }

      sessionService.logout(username);
      logger.info("Logout successful for user '{}'", username);
    } catch (Exception e) {
      logger.error("Logout failed for user '{}': {}", username, e.getMessage());
      throw new RuntimeException("Logout failed");
    }

  }

  public User register(String username, String password) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new RuntimeException("Username already exists");
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    return userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }
}
