package com.calculator.service;

import com.calculator.exception.AuthenticationException;
import com.calculator.exception.UserAlreadyLoggedInException;
import com.calculator.exception.UserNotFoundException;
import com.calculator.model.User;
import com.calculator.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final SessionService sessionService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, SessionService sessionService,  PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.sessionService = sessionService;
    this.passwordEncoder = passwordEncoder;
  }

  public User register(String username, String password) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new AuthenticationException("Username already exists");
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    return userRepository.save(user);
  }


  public void login(String username, String password) {
    // Get user and validate password
    User user = getUserByUsername(username);
    if (sessionService.isAnotherUserLoggedIn(username)) {
      logger.warn("User {} already logged in", sessionService.getCurrentLoggedInUser());
      throw new UserAlreadyLoggedInException("Another user is already logged in");
    } else if (sessionService.isUserLoggedIn(username)) {
      logger.info("User: {} already logged in, allowing to login", sessionService.getCurrentLoggedInUser());
    }
    if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
      throw new AuthenticationException("Invalid username or password");
    }

    // Session handling is now safe since we checked for other logged in users in AuthService
    sessionService.loginToSession(username);
    logger.info("User '{}' logged in successfully", username);
  }

  public void logout(String username) {
    logger.info("Logout attempt for user '{}'", username);
    try {
      sessionService.logoutFromSession(username);
      logger.info("Logout successful for user '{}'", username);
    } catch (UserNotFoundException e) {
      logger.warn("Logout failed: {}", e.getMessage());
      throw e; // Re-throw the exception for controller to handle
    }
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthenticationException("User not found: " + username));
  }
}
