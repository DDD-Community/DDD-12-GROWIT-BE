package com.growit.app.common.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.exception.ForbiddenException;
import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.BaseErrorResponse;
import com.growit.app.user.controller.dto.response.OAuthResponse;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.domain.token.service.JwtClaimKeys;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final TokenService tokenService;

  public OAuth2LoginSuccessHandler(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException {
    try {
      res.setContentType("application/json;charset=UTF-8");

      if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
        if (isPendingSignup(oAuth2User)) {
          handlePendingSignup(res, oAuth2User);
          return;
        }
      }

      User user = extractUser(authentication);
      issueTokenResponse(res, user);
    } catch (Exception e) {
      handleError(res, e);
    }
  }

  private boolean isPendingSignup(OAuth2User oAuth2User) {
    Object pending = oAuth2User.getAttributes().get(JwtClaimKeys.PENDING_SIGNUP);
    return Boolean.TRUE.equals(pending);
  }

  private void handlePendingSignup(HttpServletResponse res, OAuth2User oAuth2User) throws IOException {
    res.setStatus(HttpServletResponse.SC_OK);

    String provider = (String) oAuth2User.getAttributes().get(JwtClaimKeys.PROVIDER);
    String providerId = (String) oAuth2User.getAttributes().get(JwtClaimKeys.PROVIDER_ID);
    String email = (String) oAuth2User.getAttributes().get(JwtClaimKeys.EMAIL);
    String nickName = (String) oAuth2User.getAttributes().get(JwtClaimKeys.NICK_NAME);

    String regToken = tokenService.createRegistrationToken(provider, providerId, email);
    OAuthResponse response = OAuthResponse.builder()
      .name(nickName)
      .registrationToken(regToken)
      .build();
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

  private void issueTokenResponse(HttpServletResponse res, User user) throws IOException {
    res.setStatus(HttpServletResponse.SC_OK);

    Token token = tokenService.createToken(user);
    TokenResponse response = TokenResponse.builder()
        .accessToken(token.accessToken())
        .refreshToken(token.refreshToken())
        .build();

    new ObjectMapper().writeValue(res.getWriter(), ApiResponse.success(response));
  }

  private void handleError(HttpServletResponse res, Exception e) throws IOException {
    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    BaseErrorResponse response = BaseErrorResponse.builder()
      .message("OAuth2 authentication processing failed")
      .build();

    new ObjectMapper().writeValue(res.getWriter(), response);
  }
}
