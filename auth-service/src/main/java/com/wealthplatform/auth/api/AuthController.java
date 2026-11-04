package com.wealthplatform.auth.api;

import com.wealthplatform.auth.security.AuthenticatedUser;
import com.wealthplatform.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/auth/register")
  public AuthResponses.Tokens register(@Valid @RequestBody AuthRequests.Register request) {
    return authService.register(request);
  }

  @PostMapping("/auth/login")
  public AuthResponses.Tokens login(@Valid @RequestBody AuthRequests.Login request) {
    return authService.login(request);
  }

  @PostMapping("/auth/refresh")
  public AuthResponses.Tokens refresh(@Valid @RequestBody AuthRequests.Refresh request) {
    return authService.refresh(request.refreshToken());
  }

  @GetMapping("/users/me")
  public AuthResponses.UserProfile me(@AuthenticationPrincipal AuthenticatedUser user) {
    return authService.currentUser(user.id());
  }
}
