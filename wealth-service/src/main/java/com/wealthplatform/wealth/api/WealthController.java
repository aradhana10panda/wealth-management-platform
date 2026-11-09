package com.wealthplatform.wealth.api;

import com.wealthplatform.wealth.service.WealthService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class WealthController {
  private final WealthService wealthService;

  public WealthController(WealthService wealthService) {
    this.wealthService = wealthService;
  }

  @PostMapping("/goals")
  public GoalResponse createGoal(@Valid @RequestBody CreateGoalRequest request) {
    return wealthService.createGoal(request);
  }

  @GetMapping("/goals")
  public List<GoalResponse> getGoals(@RequestParam UUID userId) {
    return wealthService.getGoalsForUser(userId);
  }

  @PostMapping("/portfolios")
  public PortfolioResponse createPortfolio(@Valid @RequestBody CreatePortfolioRequest request) {
    return wealthService.createPortfolio(request);
  }

  @GetMapping("/portfolios")
  public List<PortfolioResponse> getPortfolios(@RequestParam UUID userId) {
    return wealthService.getPortfoliosForUser(userId);
  }

  @PostMapping("/portfolios/{portfolioId}/holdings")
  public HoldingResponse addHolding(
      @PathVariable UUID portfolioId, @Valid @RequestBody CreateHoldingRequest request) {
    return wealthService.addHolding(portfolioId, request);
  }

  @GetMapping("/portfolios/{portfolioId}/holdings")
  public List<HoldingResponse> getHoldings(@PathVariable UUID portfolioId) {
    return wealthService.getHoldingsForPortfolio(portfolioId);
  }
}
