package com.wealthplatform.auth.service;

import com.wealthplatform.auth.api.AuthRequests;
import com.wealthplatform.auth.api.AuthResponses;
import com.wealthplatform.auth.config.AuthProperties;
import com.wealthplatform.auth.domain.AppUser;
import com.wealthplatform.auth.domain.RefreshToken;
import com.wealthplatform.auth.domain.UserRole;
import com.wealthplatform.auth.repository.AppUserRepository;
import com.wealthplatform.auth.repository.RefreshTokenRepository;
import com.wealthplatform.auth.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;

@Service
public class AuthService {
  private final AppUserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthProperties properties;
  private final Clock clock;
  private final SecureRandom secureRandom = new SecureRandom();

  public AuthService(
      AppUserRepository userRepository,
      RefreshTokenRepository refreshTokenRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      AuthProperties properties,
      Clock clock) {
    this.userRepository = userRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.properties = properties;
    this.clock = clock;
  }

  private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }@Transactional
  public AuthResponses.Tokens register(AuthRequests.Register request) {
    String email = normalizeEmail(request.email());
    if (userRepository.existsByEmail(email)) {
      throw new AuthException(HttpStatus.CONFLICT, "An account with this email already exists");
    }
    if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new AuthException(HttpStatus.BAD_REQUEST, "Password must not exceed 72 UTF-8 bytes");
    }

    AppUser user =
        userRepository.save(
            new AppUser(
                request.fullName().trim(),
                email,
                passwordEncoder.encode(request.password()),
                UserRole.CLIENT));
    return issueTokens(user);
  }

  @Transactional
  public AuthResponses.Tokens login(AuthRequests.Login request) {
    AppUser user =
        userRepository
            .findByEmail(normalizeEmail(request.email()))
            .filter(
                account -> passwordEncoder.matches(request.password(), account.getPasswordHash()))
            .orElseThrow(
                () -> new AuthException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    return issueTokens(user);
  }

  @Transactional
  public AuthResponses.Tokens refresh(String rawToken) {
    String tokenHash = hash(rawToken);
    Instant now = clock.instant();
    RefreshToken current =
        refreshTokenRepository
            .findByTokenHashForUpdate(tokenHash)
            .filter(token -> token.getRevokedAt() == null && token.getExpiresAt().isAfter(now))
            .orElseThrow(
                () ->
                    new AuthException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token"));
    current.revoke(now);
    return issueTokens(current.getUser());
  }

  @Transactional(readOnly = true)
  public AuthResponses.UserProfile currentUser(UUID userId) {
    AppUser user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> new AuthException(HttpStatus.NOT_FOUND, "User account was not found"));
    return AuthResponses.UserProfile.from(user);
  }

  private AuthResponses.Tokens issueTokens(AppUser user) {
    JwtService.IssuedToken accessToken = jwtService.issue(user);
    byte[] randomBytes = new byte[32];
    secureRandom.nextBytes(randomBytes);
    String rawRefreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    Instant createdAt = clock.instant();
    Instant refreshExpiresAt = createdAt.plus(properties.refreshTokenTtl());
    refreshTokenRepository.save(
        new RefreshToken(user, hash(rawRefreshToken), refreshExpiresAt, createdAt));
    return new AuthResponses.Tokens(
        "Bearer", accessToken.value(), accessToken.expiresAt(), rawRefreshToken, refreshExpiresAt);
  }

  
}
