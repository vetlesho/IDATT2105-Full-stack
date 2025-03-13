package com.calculator.repository;

import com.calculator.model.Calculation;
import com.calculator.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<Calculation, Long> {
  Page<Calculation> findByUserOrderByTimeStamp(User user, Pageable pageable);
}
