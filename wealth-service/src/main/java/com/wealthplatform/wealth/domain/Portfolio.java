package com.wealthplatform.wealth.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "portfolios")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "total_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalValue;

    @Column(name = "cash_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal cashValue;

    @Column(name = "equity_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal equityValue;

    @Column(name = "debt_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal debtValue;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Portfolio() {
    }

    public Portfolio(UUID userId, String name, BigDecimal totalValue, BigDecimal cashValue,
                     BigDecimal equityValue, BigDecimal debtValue, boolean active) {
        this.userId = userId;
        this.name = name;
        this.totalValue = totalValue;
        this.cashValue = cashValue;
        this.equityValue = equityValue;
        this.debtValue = debtValue;
        this.active = active;
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public BigDecimal getCashValue() {
        return cashValue;
    }

    public BigDecimal getEquityValue() {
        return equityValue;
    }

    public BigDecimal getDebtValue() {
        return debtValue;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
