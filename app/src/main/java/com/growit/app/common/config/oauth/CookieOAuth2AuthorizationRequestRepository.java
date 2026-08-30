package com.growit.app.common.config.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

/** Stores OAuth state in short-lived cookies so callbacks work across Lambda instances. */
@Component
public class CookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

  private static final String AUTHORIZATION_REQUEST_COOKIE = "GROWIT_OAUTH2_REQUEST";
  private static final String REDIRECT_URI_COOKIE = "GROWIT_OAUTH2_REDIRECT";
  private static final int COOKIE_MAX_AGE_SECONDS = 180;

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    String value = getCookie(request, AUTHORIZATION_REQUEST_COOKIE);
    if (value == null) {
      return null;
    }
    byte[] serialized = Base64.getUrlDecoder().decode(value);
    return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(serialized);
  }

  @Override
  public void saveAuthorizationRequest(
      OAuth2AuthorizationRequest authorizationRequest,
      HttpServletRequest request,
      HttpServletResponse response) {
    if (authorizationRequest == null) {
      deleteCookie(response, AUTHORIZATION_REQUEST_COOKIE);
      return;
    }

    String serialized =
        Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(SerializationUtils.serialize(authorizationRequest));
    addCookie(response, AUTHORIZATION_REQUEST_COOKIE, serialized);

    String redirectUri = request.getParameter("redirect-uri");
    if (redirectUri != null && !redirectUri.isBlank()) {
      String encoded =
          Base64.getUrlEncoder()
              .withoutPadding()
              .encodeToString(redirectUri.getBytes(StandardCharsets.UTF_8));
      addCookie(response, REDIRECT_URI_COOKIE, encoded);
    }
  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(
      HttpServletRequest request, HttpServletResponse response) {
    OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
    deleteCookie(response, AUTHORIZATION_REQUEST_COOKIE);
    return authorizationRequest;
  }

  public String getRedirectUri(HttpServletRequest request) {
    String value = getCookie(request, REDIRECT_URI_COOKIE);
    if (value == null) {
      return null;
    }
    return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
  }

  public void clearRedirectUri(HttpServletResponse response) {
    deleteCookie(response, REDIRECT_URI_COOKIE);
  }

  private String getCookie(HttpServletRequest request, String name) {
    if (request.getCookies() == null) {
      return null;
    }
    for (var cookie : request.getCookies()) {
      if (name.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

  private void addCookie(HttpServletResponse response, String name, String value) {
    response.addHeader(
        "Set-Cookie",
        name
            + "="
            + value
            + "; Path=/; Max-Age="
            + COOKIE_MAX_AGE_SECONDS
            + "; HttpOnly; Secure; SameSite=Lax");
  }

  private void deleteCookie(HttpServletResponse response, String name) {
    response.addHeader(
        "Set-Cookie", name + "=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=Lax");
  }
}
