package com.wealthplatform.wealth.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "holdings")
public class Holding {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "portfolio_id", nullable = false)
  private Portfolio portfolio;

  @Column(nullable = false, length = 30)
  private String symbol;

  @Column(name = "asset_class", nullable = false, length = 30)
  private String assetClass;

  @Column(nullable = false, precision = 19, scale = 6)
  private BigDecimal units;

  @Column(name = "average_cost", nullable = false, precision = 19, scale = 6)
  private BigDecimal averageCost;

  @Column(name = "market_value", nullable = false, precision = 19, scale = 6)
  private BigDecimal marketValue;

  @Column(name = "last_updated_at", nullable = false)
  private Instant lastUpdatedAt;

  protected Holding() {}

  public Holding(
      Portfolio portfolio,
      String symbol,
      String assetClass,
      BigDecimal units,
      BigDecimal averageCost,
      BigDecimal marketValue) {
    this.portfolio = portfolio;
    this.symbol = symbol;
    this.assetClass = assetClass;
    this.units = units;
    this.averageCost = averageCost;
    this.marketValue = marketValue;
  }

  @PrePersist
  @PreUpdate
  void touch() {
    this.lastUpdatedAt = Instant.now();
  }

  public UUID getId() {
    return id;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getAssetClass() {
    return assetClass;
  }

  public BigDecimal getUnits() {
    return units;
  }

  public BigDecimal getAverageCost() {
    return averageCost;
  }

  public BigDecimal getMarketValue() {
    return marketValue;
  }

  public Instant getLastUpdatedAt() {
    return lastUpdatedAt;
  }
}
