package com.calculator.service;

import com.calculator.exception.AuthenticationException;
import com.calculator.exception.UserAlreadyLoggedInException;
import com.calculator.model.User;
import com.calculator.repository.UserRepository;
import com.calculator.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final UserRepository userRepository;
  private final SessionService sessionService;
  private final JwtTokenUtil jwtTokenUtil;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  public AuthService(UserRepository userRepository,
                     SessionService sessionService,
                     JwtTokenUtil jwtTokenUtil,
                     AuthenticationManager authenticationManager,
                     PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.sessionService = sessionService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
  }

  public Map<String, Object> registerUser(String username, String password) {
    logger.info("Registering user: {}", username);
    try {

      if (userRepository.findByUsername(username).isPresent()) {
        throw new AuthenticationException("Username already exists");
      }

      User user = new User();
      user.setUsername(username);
      user.setPassword(passwordEncoder.encode(password));
      User registeredUser = userRepository.save(user);

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

      sessionService.loginToSession(username);

      // Generate JWT token
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String token = jwtTokenUtil.generateToken(userDetails);

      Map<String, Object> response = new HashMap<>();
      response.put("token", token);
      response.put("username", username);
      response.put("expiresIn", 300); // 5 minutes in seconds
      response.put("message", "Login success");

      logger.info("User '{}' logged in successfully", username);
      return response;
    } catch (Exception e) {
      logger.error("Login failed for user {}: {}", username, e.getMessage());
      throw new AuthenticationException("Authentication failed: " + e.getMessage());
    }
  }

  public Map<String, Object> logoutUser(String token) {
    logger.info("Logout attempt received");
    try {
      String username = jwtTokenUtil.extractUsername(token.substring(7));
      sessionService.logoutFromSession(username);
      logger.info("User '{}' logged out successfully", username);
      return Map.of("message", "Logout success");
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
      User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new AuthenticationException("User not found: " + username));

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