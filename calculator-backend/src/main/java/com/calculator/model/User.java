package com.calculator.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Calculation> calculations = new ArrayList<>();

  public User() {}

  public User(String password, String username) {
    this.password = password;
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public Long getId() {
    return id;
  }

  public String getPassword() {
    return password;
  }

  public List<Calculation> getCalculations() {
    return calculations;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setCalculations(List<Calculation> calculations) {
    this.calculations = calculations;
  }
}
