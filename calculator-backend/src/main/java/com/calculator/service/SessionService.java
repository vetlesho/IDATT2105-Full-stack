package com.calculator.service;

import com.calculator.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
  private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
  private final ConcurrentHashMap<String, Boolean> loggedInUsers = new ConcurrentHashMap<>();

  public boolean login(String username) {
    if (loggedInUsers.isEmpty()) {
      loggedInUsers.put(username, true);
      logger.info("User '{}' is is the session", username);
      return true;
    }
    if (loggedInUsers.containsKey(username)) {
      logger.info("User '{}' is already logged in - allowing reconnection", username);
      return true; // Allow reconnection for same user
    }

    String loggedInUser = loggedInUsers.keySet().iterator().next();
    logger.warn("Login attempt for user '{}' failed - '{}' is already logged in",
            username, loggedInUser);
    return false;
  }

  public void logout(String username) {
    if (!isUserLoggedIn(username)) {
      logger.warn("Cannot logout user '{}' - not logged in", username);
      throw new UserNotFoundException("User not found in active sessions");
    }

    loggedInUsers.remove(username);
    logger.info("User '{}' has been logged out", username);
  }

  public boolean isUserLoggedIn(String username) {
    return loggedInUsers.containsKey(username);
  }

  public void clearSessions() {
    loggedInUsers.clear();
    logger.info("All sessions cleared");
  }
}
