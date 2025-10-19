package com.growit.app.common.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.exception.ForbiddenException;
import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.BaseErrorResponse;
import com.growit.app.user.controller.dto.response.OAuthResponse;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.domain.token.service.JwtClaimKeys;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.service.UserTokenSaver;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private static final String ACCESS_TOKEN_PARAM = "accessToken";
  private static final String REFRESH_TOKEN_PARAM = "refreshToken";
  private static final String REGISTRATION_TOKEN_PARAM = "registrationToken";
  private static final String NAME_PARAM = "name";
  private static final String REDIRECT_URI_SESSION_KEY = "OAUTH2_REDIRECT_URI";

  private static final List<String> ALLOWED_REDIRECT_HOSTS =
      List.of("localhost:3000", "grow-it.me");

  private final UserTokenSaver userTokenSaver;
  private final TokenService tokenService;
  private final OAuth2AuthorizedClientService authorizedClientService;

  public OAuth2LoginSuccessHandler(
      UserTokenSaver userTokenSaver,
      TokenService tokenService,
      OAuth2AuthorizedClientService authorizedClientService) {
    this.userTokenSaver = userTokenSaver;
    this.tokenService = tokenService;
    this.authorizedClientService = authorizedClientService;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest req, HttpServletResponse res, Authentication authentication)
      throws IOException {
    try {
      res.setContentType("application/json;charset=UTF-8");

      if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
        if (isPendingSignup(oAuth2User)) {
          handlePendingSignup(req, res, oAuth2User);
          clearSessionAndAuthorizedClient(req, res, authentication);
          return;
        }
      }

      User user = extractUser(authentication);
      issueTokenResponse(req, res, user, authentication);
      clearSessionAndAuthorizedClient(req, res, authentication);
    } catch (Exception e) {
      handleError(res, e);
    }
  }

  private boolean isPendingSignup(OAuth2User oAuth2User) {
    Object pending = oAuth2User.getAttributes().get(JwtClaimKeys.PENDING_SIGNUP);
    return Boolean.TRUE.equals(pending);
  }

  private void handlePendingSignup(
      HttpServletRequest req, HttpServletResponse res, OAuth2User oAuth2User) throws IOException {
    String provider = (String) oAuth2User.getAttributes().get(JwtClaimKeys.PROVIDER);
    String providerId = (String) oAuth2User.getAttributes().get(JwtClaimKeys.PROVIDER_ID);
    String email = (String) oAuth2User.getAttributes().get(JwtClaimKeys.EMAIL);
    String nickName = (String) oAuth2User.getAttributes().get(JwtClaimKeys.NICK_NAME);

    String regToken = tokenService.createRegistrationToken(provider, providerId, email);

    // If redirect_uri is provided, prefer redirect-based flow (for browser navigation starts)
    String redirectUri = getRedirectUriFromSession(req);
    if (redirectUri != null && !redirectUri.isBlank()) {
      validateRedirectUri(redirectUri);
      String location =
          redirectUri
              + "#"
              + REGISTRATION_TOKEN_PARAM
              + "="
              + URLEncoder.encode(regToken, StandardCharsets.UTF_8)
              + "&"
              + NAME_PARAM
              + "="
              + URLEncoder.encode(nickName != null ? nickName : "", StandardCharsets.UTF_8);
      res.setStatus(HttpServletResponse.SC_FOUND); // 302
      res.setHeader("Location", location);
      return;
    }

    // Fallback: API (XHR) flow returns JSON body
    res.setStatus(HttpServletResponse.SC_OK);
    OAuthResponse response =
        OAuthResponse.builder().name(nickName).registrationToken(regToken).build();
    new ObjectMapper().writeValue(res.getWriter(), ApiResponse.success(response));
  }

  private User extractUser(Authentication authentication) {
    Object principal = authentication.getPrincipal();

    if (principal instanceof OAuth2User oAuth2User) {
      Object attrUser = oAuth2User.getAttributes().get(JwtClaimKeys.USER);
      if (attrUser instanceof User user) {
        return user;
      }
    }

    throw new ForbiddenException("Unsupported authentication principal");
  }

  private void issueTokenResponse(
      HttpServletRequest req, HttpServletResponse res, User user, Authentication authentication)
      throws IOException {
    Token token = tokenService.createToken(user);
    userTokenSaver.saveUserToken(user.getId(), token);

    String redirectUri = getRedirectUriFromSession(req);
    if (redirectUri != null && !redirectUri.isBlank()) {
      validateRedirectUri(redirectUri);
      // Redirect flow: attach tokens in fragment (not sent to servers via Referer)
      String location =
          redirectUri
              + "#"
              + ACCESS_TOKEN_PARAM
              + "="
              + URLEncoder.encode(token.accessToken(), StandardCharsets.UTF_8)
              + "&"
              + REFRESH_TOKEN_PARAM
              + "="
              + URLEncoder.encode(token.refreshToken(), StandardCharsets.UTF_8);
      res.setStatus(HttpServletResponse.SC_FOUND); // 302
      res.setHeader("Location", location);
      return;
    }

    // API (XHR) flow: return JSON body
    res.setStatus(HttpServletResponse.SC_OK);
    TokenResponse response =
        TokenResponse.builder()
            .accessToken(token.accessToken())
            .refreshToken(token.refreshToken())
            .build();
    new ObjectMapper().writeValue(res.getWriter(), ApiResponse.success(response));
  }

  private String getRedirectUriFromSession(HttpServletRequest req) {
    var session = req.getSession(false);
    if (session != null) {
      return (String) session.getAttribute(REDIRECT_URI_SESSION_KEY);
    }
    return null;
  }

  private void validateRedirectUri(String redirectUri) {
    try {
      URI uri = URI.create(redirectUri);
      String host = uri.getHost();
      int port = uri.getPort();

      String hostWithPort = port != -1 ? host + ":" + port : host;

      if (host == null
          || (!ALLOWED_REDIRECT_HOSTS.contains(host.toLowerCase())
              && !ALLOWED_REDIRECT_HOSTS.contains(hostWithPort.toLowerCase()))) {
        throw new ForbiddenException("Invalid redirect URI: " + redirectUri);
      }
    } catch (IllegalArgumentException e) {
      throw new ForbiddenException("Malformed redirect URI: " + redirectUri);
    }
  }

  private void handleError(HttpServletResponse res, Exception e) throws IOException {
    int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    String message = "OAuth2 authentication processing failed";

    if (e instanceof ForbiddenException) {
      status = HttpServletResponse.SC_FORBIDDEN;
      message = e.getMessage();
    }

    res.setStatus(status);
    BaseErrorResponse response = BaseErrorResponse.builder().message(message).build();
    new ObjectMapper().writeValue(res.getWriter(), response);
  }

  private void clearSessionAndAuthorizedClient(
      HttpServletRequest req, HttpServletResponse res, Authentication authentication) {
    try {
      if (authentication instanceof OAuth2AuthenticationToken token) {
        // Remove cached authorized client (access/refresh tokens, etc.) so subsequent requests
        // don't use session cache
        authorizedClientService.removeAuthorizedClient(
            token.getAuthorizedClientRegistrationId(), token.getName());
      }
      // Invalidate session if exists
      var session = req.getSession(false);
      if (session != null) {
        session.invalidate();
      }
      // Remove JSESSIONID cookie from client
      Cookie cookie = new Cookie("JSESSIONID", null);
      cookie.setPath("/");
      cookie.setMaxAge(0);
      cookie.setHttpOnly(true);
      res.addCookie(cookie);
    } catch (Exception ignore) {
      // best-effort cleanup; do not block success flow
    }
  }
}
