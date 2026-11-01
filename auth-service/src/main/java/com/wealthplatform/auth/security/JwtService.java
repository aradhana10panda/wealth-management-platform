package com.wealthplatform.auth.security;

import com.wealthplatform.auth.config.AuthProperties;
import com.wealthplatform.auth.domain.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey signingKey;
    private final AuthProperties properties;
    private final Clock clock;

    public JwtService(AuthProperties properties, Clock clock) {
        byte[] secret = Decoders.BASE64.decode(properties.jwtSecret());
        if (secret.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET must decode to at least 32 bytes");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret);
        this.properties = properties;
        this.clock = clock;
    }

    public IssuedToken issue(AppUser user) {
        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(properties.accessTokenTtl());
        String token = Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
        return new IssuedToken(token, expiresAt);
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public record IssuedToken(String value, Instant expiresAt) {
    }
}
