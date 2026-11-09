package com.wealthplatform.wealth.repository;

import com.wealthplatform.wealth.domain.Portfolio;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
  List<Portfolio> findByUserIdOrderByUpdatedAtDesc(UUID userId);
}
