package com.wealthplatform.wealth.api;

import com.wealthplatform.wealth.domain.Holding;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record HoldingResponse(
    UUID id,
    UUID portfolioId,
    String symbol,
    String assetClass,
    BigDecimal units,
    BigDecimal averageCost,
    BigDecimal marketValue,
    Instant lastUpdatedAt) {
  public static HoldingResponse from(Holding holding) {
    return new HoldingResponse(
        holding.getId(),
        holding.getPortfolio().getId(),
        holding.getSymbol(),
        holding.getAssetClass(),
        holding.getUnits(),
        holding.getAverageCost(),
        holding.getMarketValue(),
        holding.getLastUpdatedAt());
  }
}
