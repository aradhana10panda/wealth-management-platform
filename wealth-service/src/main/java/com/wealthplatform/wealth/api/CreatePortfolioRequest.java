package com.wealthplatform.wealth.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record CreatePortfolioRequest(
    @NotNull UUID userId,
    @NotBlank @Size(max = 120) String name,
    @NotNull BigDecimal totalValue,
    @NotNull BigDecimal cashValue,
    @NotNull BigDecimal equityValue,
    @NotNull BigDecimal debtValue,
    Boolean active) {}
