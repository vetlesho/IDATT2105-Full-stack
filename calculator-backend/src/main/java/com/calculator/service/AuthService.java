package com.calculator.service;

import com.calculator.exception.AuthenticationException;
import com.calculator.exception.UserAlreadyLoggedInException;
import com.calculator.model.User;
import com.calculator.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final UserService userService;
  private final SessionService sessionService;
  private final JwtTokenUtil jwtTokenUtil;
  private final AuthenticationManager authenticationManager;

  public AuthService(UserService userService, SessionService sessionService, JwtTokenUtil jwtTokenUtil,
                     AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.sessionService = sessionService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.authenticationManager = authenticationManager;
  }

  public Map<String, Object> registerUser(String username, String password) {
    logger.info("Registering user: {}", username);
    try {
      User registeredUser = userService.register(username, password);

      Map<String, Object> response = new HashMap<>();
      response.put("username", registeredUser.getUsername());
      response.put("message", "User registered successfully");

      logger.info("User registered successfully: {}", username);
      return response;
    } catch (Exception e) {
      logger.error("Registration failed for user {}: {}", username, e.getMessage());
      throw e;
    }
  }

  public Map<String, Object> loginUser(String username, String password) {
    logger.info("Processing login request for user: {}", username);

    try {
      // Check if another user is logged in before proceeding
      if (sessionService.isAnotherUserLoggedIn(username)) {
        logger.warn("Login attempt for user '{}' denied - user '{}' is already logged in",
                username, sessionService.getCurrentLoggedInUser());
        throw new UserAlreadyLoggedInException("Another user is already logged in: " +
                sessionService.getCurrentLoggedInUser());
      }

      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(username, password)
      );

      userService.login(username, password);

      // Generate JWT token
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String token = jwtTokenUtil.generateToken(userDetails);

      Map<String, Object> response = new HashMap<>();
      response.put("token", token);
      response.put("username", username);
      response.put("expiresIn", 300); // 5 minutes in seconds

      logger.info("User '{}' logged in successfully", username);
      return response;
    } catch (UserAlreadyLoggedInException e) {
      // Re-throw this specific exception
      logger.warn("Login failed: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("Login failed for user {}: {}", username, e.getMessage());
      throw new AuthenticationException("Authentication failed: " + e.getMessage());
    }
  }

  public Map<String, Object> logoutUser(String token) {
    logger.info("Logout attempt received");
    try {
      String username = jwtTokenUtil.extractUsername(token.substring(7));
      userService.logout(username);
      logger.info("User '{}' logged out successfully", username);
      return Map.of("message", "Logged out successfully");
    } catch (Exception e) {
      logger.error("Logout failed: {}", e.getMessage());
      throw e;
    }
  }

  public Map<String, Object> refreshToken(String authHeader) {
    logger.info("Token refresh request received");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      logger.warn("Missing or invalid Authorization header for token refresh");
      throw new AuthenticationException("Invalid authorization");
    }

    try {
      String username = jwtTokenUtil.extractUsername(authHeader.substring(7));

      // Get user from database to validate they still exist
      User user = userService.getUserByUsername(username);

      // Create UserDetails object for token generation
      UserDetails userDetails = org.springframework.security.core.userdetails.User
              .withUsername(username)
              .password(user.getPassword())
              .authorities("USER")
              .build();

      // Generate new token with fresh expiry
      String newToken = jwtTokenUtil.generateToken(userDetails);

      Map<String, Object> response = new HashMap<>();
      response.put("token", newToken);
      response.put("username", username);
      response.put("expiresIn", 300);

      logger.info("Token refreshed successfully for user: {}", username);
      return response;
    } catch (Exception e) {
      logger.error("Couldn't refresh token: {}", e.getMessage());
      throw e;
    }
  }
}