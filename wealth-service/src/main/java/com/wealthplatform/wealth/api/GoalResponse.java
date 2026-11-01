package com.wealthplatform.wealth.api;

import com.wealthplatform.wealth.domain.InvestmentGoal;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record GoalResponse(
        UUID id,
        UUID userId,
        String name,
        BigDecimal targetAmount,
        BigDecimal currentAmount,
        LocalDate targetDate,
        String riskTolerance,
        String status,
        Instant createdAt,
        Instant updatedAt
) {
    public static GoalResponse from(InvestmentGoal goal) {
        return new GoalResponse(
                goal.getId(),
                goal.getUserId(),
                goal.getName(),
                goal.getTargetAmount(),
                goal.getCurrentAmount(),
                goal.getTargetDate(),
                goal.getRiskTolerance(),
                goal.getStatus().name(),
                goal.getCreatedAt(),
                goal.getUpdatedAt()
        );
    }
}
