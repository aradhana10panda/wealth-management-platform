package com.wealthplatform.wealth.repository;

import com.wealthplatform.wealth.domain.InvestmentGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvestmentGoalRepository extends JpaRepository<InvestmentGoal, UUID> {
    List<InvestmentGoal> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
