package com.growit.app.common.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private TokenService tokenService;
  @Override
  public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException {
    Token token = tokenService.createToken((User)authentication.getPrincipal());
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
