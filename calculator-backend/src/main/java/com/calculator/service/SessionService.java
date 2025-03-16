package com.calculator.service;

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
    String loggedInUser = loggedInUsers.keySet().iterator().next();
    logger.warn("Login attempt for user '{}' failed - '{}' is already logged in",
            username, loggedInUser);
    return false;
  }

  public void logout(String username) {
    Boolean removed = loggedInUsers.remove(username);
    if (removed != null) {
      logger.info("User '{}' removed from session", username);
    } else {
      logger.warn("Logout attempt for user '{}' failed - user was not in session", username);
    }
  }

  public boolean isUserLoggedIn(String username) {
    return loggedInUsers.containsKey(username);
  }

  public void clearSessions() {
    loggedInUsers.clear();
    logger.info("All sessions cleared");
  }
}
