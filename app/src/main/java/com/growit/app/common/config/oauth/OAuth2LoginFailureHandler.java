package com.growit.app.common.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private static final String REDIRECT_URI_SESSION_KEY = "OAUTH2_REDIRECT_URI";
  private static final String FAILURE_PATH = "/login?error=oauth";

  private static final List<String> ALLOWED_REDIRECT_HOSTS =
      List.of("localhost:3000", "grow-it.me", "devweb.grow-it.me");

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest req, HttpServletResponse res, AuthenticationException exception)
      throws IOException, ServletException {

    log.error("OAuth2 Login Failed. Error: {}", exception.getMessage(), exception);

    String location = resolveFailureLocation(req);
    getRedirectStrategy().sendRedirect(req, res, location);
  }

  private String resolveFailureLocation(HttpServletRequest req) {
    String redirectUri = getRedirectUriFromSession(req);
    if (redirectUri == null || redirectUri.isBlank()) {
      return FAILURE_PATH;
    }
    try {
      URI uri = URI.create(redirectUri);
      String host = uri.getHost();
      if (host == null) {
        return FAILURE_PATH;
      }
      int port = uri.getPort();
      String hostWithPort = port != -1 ? host + ":" + port : host;
      boolean allowed =
          ALLOWED_REDIRECT_HOSTS.contains(host.toLowerCase())
              || ALLOWED_REDIRECT_HOSTS.contains(hostWithPort.toLowerCase());
      if (!allowed) {
        return FAILURE_PATH;
      }
      return uri.getScheme() + "://" + hostWithPort + FAILURE_PATH;
    } catch (IllegalArgumentException e) {
      return FAILURE_PATH;
    }
  }

  private String getRedirectUriFromSession(HttpServletRequest req) {
    var session = req.getSession(false);
    if (session != null) {
      return (String) session.getAttribute(REDIRECT_URI_SESSION_KEY);
    }
    return null;
  }
}
