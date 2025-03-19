package com.calculator.service;

import com.calculator.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SessionService {
  private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
  private String currentLoggedInUser = null;

  public void loginToSession(String username) {
    currentLoggedInUser = username;
    logger.info("User {} is now in session", username);
  }

  public void logoutFromSession(String username) {
    if (!isSessionBusy()) {
      logger.warn("Cannot logout - no user is logged in");
      throw new UserNotFoundException("No user is currently logged in");
    }

    if (isAnotherUserLoggedIn(username)) {
      logger.warn("Cannot logout user '{}' - current logged in user is '{}'",
              username, currentLoggedInUser);
      throw new UserNotFoundException("User not found in active session");
    } else {
      clearSession();
      logger.info("User '{}' has been logged out from the session", username);
    }
  }

  public boolean isUserLoggedIn(String username) {
    return username.equals(currentLoggedInUser);
  }

  public boolean isSessionBusy() {
    return currentLoggedInUser != null;
  }

  public String getCurrentLoggedInUser() {
    return currentLoggedInUser;
  }

  public boolean isAnotherUserLoggedIn(String username) {
    return currentLoggedInUser != null && !currentLoggedInUser.equals(username);
  }

  private void clearSession() {
    currentLoggedInUser = null;
  }
}
