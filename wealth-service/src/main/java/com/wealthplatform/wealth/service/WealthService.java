package com.wealthplatform.wealth.service;

import com.wealthplatform.wealth.api.*;
import com.wealthplatform.wealth.domain.GoalStatus;
import com.wealthplatform.wealth.domain.Holding;
import com.wealthplatform.wealth.domain.InvestmentGoal;
import com.wealthplatform.wealth.domain.Portfolio;
import com.wealthplatform.wealth.repository.HoldingRepository;
import com.wealthplatform.wealth.repository.InvestmentGoalRepository;
import com.wealthplatform.wealth.repository.PortfolioRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WealthService {
  private final InvestmentGoalRepository goalRepository;
  private final PortfolioRepository portfolioRepository;
  private final HoldingRepository holdingRepository;

  public WealthService(
      InvestmentGoalRepository goalRepository,
      PortfolioRepository portfolioRepository,
      HoldingRepository holdingRepository) {
    this.goalRepository = goalRepository;
    this.portfolioRepository = portfolioRepository;
    this.holdingRepository = holdingRepository;
  }

  @Transactional
  public GoalResponse createGoal(CreateGoalRequest request) {
    InvestmentGoal goal =
        new InvestmentGoal(
            request.userId(),
            request.name(),
            request.targetAmount(),
            request.currentAmount(),
            request.targetDate(),
            request.riskTolerance(),
            request.status() == null ? GoalStatus.ACTIVE : GoalStatus.valueOf(request.status()));
    InvestmentGoal saved = goalRepository.save(goal);
    return GoalResponse.from(saved);
  }

  @Transactional(readOnly = true)
  public List<GoalResponse> getGoalsForUser(UUID userId) {
    return goalRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(GoalResponse::from)
        .toList();
  }

  @Transactional
  public PortfolioResponse createPortfolio(CreatePortfolioRequest request) {
    Portfolio portfolio =
        new Portfolio(
            request.userId(),
            request.name(),
            request.totalValue(),
            request.cashValue(),
            request.equityValue(),
            request.debtValue(),
            request.active() == null || request.active());
    Portfolio saved = portfolioRepository.save(portfolio);
    return PortfolioResponse.from(saved);
  }

  @Transactional(readOnly = true)
  public List<PortfolioResponse> getPortfoliosForUser(UUID userId) {
    return portfolioRepository.findByUserIdOrderByUpdatedAtDesc(userId).stream()
        .map(PortfolioResponse::from)
        .toList();
  }

  @Transactional
  public HoldingResponse addHolding(UUID portfolioId, CreateHoldingRequest request) {
    Portfolio portfolio =
        portfolioRepository
            .findById(portfolioId)
            .orElseThrow(() -> new WealthException(HttpStatus.NOT_FOUND, "Portfolio not found"));

    Holding holding =
        new Holding(
            portfolio,
            request.symbol(),
            request.assetClass(),
            request.units(),
            request.averageCost(),
            request.marketValue());
    Holding saved = holdingRepository.save(holding);
    return HoldingResponse.from(saved);
  }

  @Transactional(readOnly = true)
  public List<HoldingResponse> getHoldingsForPortfolio(UUID portfolioId) {
    return holdingRepository.findByPortfolioIdOrderBySymbolAsc(portfolioId).stream()
        .map(HoldingResponse::from)
        .toList();
  }
}
