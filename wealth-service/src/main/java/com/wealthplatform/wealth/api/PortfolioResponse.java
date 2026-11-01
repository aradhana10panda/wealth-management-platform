package com.wealthplatform.wealth.api;

import com.wealthplatform.wealth.domain.Portfolio;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PortfolioResponse(
        UUID id,
        UUID userId,
        String name,
        BigDecimal totalValue,
        BigDecimal cashValue,
        BigDecimal equityValue,
        BigDecimal debtValue,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static PortfolioResponse from(Portfolio portfolio) {
        return new PortfolioResponse(
                portfolio.getId(),
                portfolio.getUserId(),
                portfolio.getName(),
                portfolio.getTotalValue(),
                portfolio.getCashValue(),
                portfolio.getEquityValue(),
                portfolio.getDebtValue(),
                portfolio.isActive(),
                portfolio.getCreatedAt(),
                portfolio.getUpdatedAt()
        );
    }
}
