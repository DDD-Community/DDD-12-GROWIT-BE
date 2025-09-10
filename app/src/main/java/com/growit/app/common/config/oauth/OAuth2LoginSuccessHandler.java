package com.growit.app.common.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final TokenService tokenService;

  public OAuth2LoginSuccessHandler(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException {
    if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
      Object pending = oAuth2User.getAttributes().get("pendingSignup");
      if (Boolean.TRUE.equals(pending)) {
        // 아직 가입 전: 토큰 발급하지 않고 needsSignup 응답 반환
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json;charset=UTF-8");
        String provider = (String)oAuth2User.getAttributes().get("provider");
        String providerId = (String)oAuth2User.getAttributes().get("providerId");
        String email = (String)oAuth2User.getAttributes().get("email");
        String regToken = tokenService.createRegistrationToken(provider, providerId, email);
        new ObjectMapper().writeValue(res.getWriter(), Map.of(
            "registrationToken", regToken,
          "name", oAuth2User.getAttributes().get("nickName"),
          "email", email
        ));
        return;
      }
    }

    // 정상 가입된 사용자일 경우 토큰 발급
    Object principal = authentication.getPrincipal();
    User user;
    if (principal instanceof OAuth2User oAuth2User) {
      Object attrUser = oAuth2User.getAttributes().get("user");
      if (attrUser instanceof User u) {
        user = u;
      } else {
        throw new IllegalStateException("OAuth2User does not contain domain User");
      }
    } else if (principal instanceof User u) {
      user = u;
    } else {
      throw new IllegalStateException("Unsupported principal: " + principal);
    }

    Token token = tokenService.createToken(user);
    TokenResponse tokenResponse = TokenResponse.builder()
        .accessToken(token.accessToken())
        .refreshToken(token.refreshToken())
        .build();

    res.setStatus(HttpServletResponse.SC_OK);
    res.setContentType("application/json;charset=UTF-8");

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.writeValue(res.getWriter(), tokenResponse);
  }
}
