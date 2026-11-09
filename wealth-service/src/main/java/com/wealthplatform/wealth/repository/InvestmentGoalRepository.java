package com.wealthplatform.wealth.repository;

import com.wealthplatform.wealth.domain.InvestmentGoal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentGoalRepository extends JpaRepository<InvestmentGoal, UUID> {
  List<InvestmentGoal> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
