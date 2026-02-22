package com.growit.app.common.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(
      HttpServletRequest req, HttpServletResponse res, AuthenticationException exception)
      throws IOException, ServletException {

    log.error("OAuth2 Login Failed. Error: {}", exception.getMessage(), exception);

    getRedirectStrategy().sendRedirect(req, res, "/login?error=oauth");
  }
}
