package com.wealthplatform.wealth.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateGoalRequest(
    @NotNull UUID userId,
    @NotBlank @Size(max = 120) String name,
    @NotNull BigDecimal targetAmount,
    @NotNull BigDecimal currentAmount,
    @NotNull LocalDate targetDate,
    @NotBlank @Size(max = 20) String riskTolerance,
    String status) {}
