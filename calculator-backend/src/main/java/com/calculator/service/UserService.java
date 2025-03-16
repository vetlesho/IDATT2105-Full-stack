package com.calculator.service;

import com.calculator.exception.AuthenticationException;
import com.calculator.exception.UserAlreadyLoggedInException;
import com.calculator.exception.UserNotFoundException;
import com.calculator.model.User;
import com.calculator.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    logger.debug("Login attempt for user: '{}'", username);

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
              logger.error("Authentication failed for user '{}'", username);
              return new AuthenticationException("Invalid username");
            });

    if (!user.getPassword().equals(password)) {
      logger.error("Authentication failed for user '{}' - invalid password", username);
      throw new AuthenticationException("Invalid password");
    }

    if (!sessionService.login(username)) {
      logger.warn("Login blocked - another user is already in session");
      throw new UserAlreadyLoggedInException("Another user is currently logged in");
    }

    logger.info("Login successful for user '{}'", username);
    return user;
  }


  public void logout(String username) {
    try {
      logger.info("Logout attempt for user '{}'", username);
      if (sessionService.isUserLoggedIn(username)) {
        logger.warn("Cannot logout user '{}' - not logged in", username);
        throw new UserNotFoundException("User not logged in");
      }

      sessionService.logout(username);
      logger.info("Logout successful for user '{}'", username);
    } catch (Exception e) {
      logger.error("Logout failed for user '{}': {}", username, e.getMessage());
      throw new AuthenticationException("Logout failed");
    }

  }

  public User register(String username, String password) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new AuthenticationException("Username already exists");
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    return userRepository.save(user);
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthenticationException("User not found: " + username));

  }


}
