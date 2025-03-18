package com.calculator.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");

    String username = null;
    String token = null;

    try {
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        token = authorizationHeader.substring(7);
        username = jwtTokenUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

          if (jwtTokenUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
        }
      }

      filterChain.doFilter(request, response);

    } catch (ExpiredJwtException e) {
      logger.error("JWT token has expired: {}", e.getMessage());
      handleAuthError(response, "Token expired, please login again", HttpServletResponse.SC_UNAUTHORIZED);
    } catch (MalformedJwtException | SignatureException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
      handleAuthError(response, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
    } catch (Exception e) {
      logger.error("Authentication error: {}", e.getMessage());
      handleAuthError(response, "Authentication error", HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  private void handleAuthError(HttpServletResponse response, String message, int statusCode) throws IOException {
    response.setStatus(statusCode);
    response.setContentType("application/json");

    PrintWriter out = response.getWriter();
    out.print("{\"error\":\"" + message + "\",\"status\":" + statusCode + "}");
    out.flush();
  }
}
