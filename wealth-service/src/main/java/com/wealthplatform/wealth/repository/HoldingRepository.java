package com.wealthplatform.wealth.repository;

import com.wealthplatform.wealth.domain.Holding;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoldingRepository extends JpaRepository<Holding, UUID> {
  List<Holding> findByPortfolioIdOrderBySymbolAsc(UUID portfolioId);
}
