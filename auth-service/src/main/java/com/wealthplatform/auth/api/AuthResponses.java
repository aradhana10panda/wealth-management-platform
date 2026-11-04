package com.wealthplatform.auth.api;

import com.wealthplatform.auth.domain.AppUser;
import java.time.Instant;
import java.util.UUID;

public final class AuthResponses {
  private AuthResponses() {}

  public record Tokens(
      String tokenType,
      String accessToken,
      Instant accessTokenExpiresAt,
      String refreshToken,
      Instant refreshTokenExpiresAt) {}

  public record UserProfile(
      UUID id, String fullName, String email, String role, Instant createdAt) {
    public static UserProfile from(AppUser user) {
      return new UserProfile(
          user.getId(),
          user.getFullName(),
          user.getEmail(),
          user.getRole().name(),
          user.getCreatedAt());
    }
  }
}
