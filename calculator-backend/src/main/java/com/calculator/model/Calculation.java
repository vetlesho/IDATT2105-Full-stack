package com.calculator.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calculations")
public class Calculation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String expression;

  @Column(nullable = false)
  private String result;

  @Column(nullable = false)
  private LocalDateTime timeStamp;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Calculation() {}

  public Calculation(String expression, String result, LocalDateTime timeStamp, User user) {
    this.expression = expression;
    this.result = result;
    this.timeStamp = timeStamp;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public String getExpression() {
    return expression;
  }

  public String getResult() {
    return result;
  }

  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  public User getUser() {
    return user;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setTimeStamp(LocalDateTime timeStamp) {
    this.timeStamp = timeStamp;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
