package com.wealthplatform.wealth.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateHoldingRequest(
        @NotBlank @Size(max = 30) String symbol,
        @NotBlank @Size(max = 30) String assetClass,
        @NotNull BigDecimal units,
        @NotNull BigDecimal averageCost,
        @NotNull BigDecimal marketValue
) {
}
