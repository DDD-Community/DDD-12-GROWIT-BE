package com.growit.app.common.config.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2AuthorizationRequestResolver
    implements OAuth2AuthorizationRequestResolver {

  private static final String REDIRECT_URI_PARAM = "redirect-uri";
  private static final String REDIRECT_URI_SESSION_KEY = "OAUTH2_REDIRECT_URI";
  private final OAuth2AuthorizationRequestResolver defaultResolver;

  public CustomOAuth2AuthorizationRequestResolver(
      ClientRegistrationRepository clientRegistrationRepository) {
    this.defaultResolver =
        new DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository, "/oauth2/authorization");
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
    return customizeAuthorizationRequest(authorizationRequest, request);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(
      HttpServletRequest request, String clientRegistrationId) {
    OAuth2AuthorizationRequest authorizationRequest =
        defaultResolver.resolve(request, clientRegistrationId);
    return customizeAuthorizationRequest(authorizationRequest, request);
  }

  private OAuth2AuthorizationRequest customizeAuthorizationRequest(
      OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request) {
    if (authorizationRequest == null) {
      return null;
    }

    String redirectUri = request.getParameter(REDIRECT_URI_PARAM);
    if (redirectUri != null && !redirectUri.isBlank()) {
      HttpSession session = request.getSession(true);
      session.setAttribute(REDIRECT_URI_SESSION_KEY, redirectUri);
    }

    return authorizationRequest;
  }
}
