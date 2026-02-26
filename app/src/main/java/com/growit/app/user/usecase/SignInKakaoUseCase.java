package com.growit.app.user.usecase;

import com.growit.app.common.config.oauth.KakaoKeys;
import com.growit.app.user.controller.dto.response.OAuthResponse;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.domain.token.service.KakaoIdTokenValidator;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.service.UserTokenSaver;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.OAuthCommand;
import com.growit.app.user.domain.user.dto.SignInKakaoCommand;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInKakaoUseCase {

  private final KakaoIdTokenValidator kakaoIdTokenValidator;
  private final OAuthLinkUseCase oAuthLinkUseCase;
  private final TokenGenerator tokenGenerator;
  private final UserTokenSaver userTokenSaver;
  private final TokenService tokenService;

  @Transactional
  public SignInKakaoResult execute(SignInKakaoCommand command) {
    Map<String, Object> attributes =
        kakaoIdTokenValidator.parseAndVerifyIdToken(command.idToken(), command.nonce());
    String sub = (String) attributes.get(KakaoKeys.SUB);
    String email = (String) attributes.get(KakaoKeys.EMAIL);
    if (email == null || email.isBlank()) {
      email = sub + "@kakao.com";
    }

    OAuthCommand oauthCommand =
        new OAuthCommand(email, KakaoKeys.PROVIDER_NAME, sub, command.refreshToken());
    Optional<User> existingUser = oAuthLinkUseCase.execute(oauthCommand);

    if (existingUser.isPresent()) {
      User user = existingUser.get();
      Token token = tokenGenerator.createToken(user);
      userTokenSaver.saveUserToken(user.getId(), token);

      TokenResponse tokenResponse =
          TokenResponse.builder()
              .accessToken(token.accessToken())
              .refreshToken(token.refreshToken())
              .build();

      return new SignInKakaoResult(false, tokenResponse, null);
    } else {
      String regToken =
          tokenService.createRegistrationToken(
              KakaoKeys.PROVIDER_NAME, sub, email, command.refreshToken());

      OAuthResponse oauthResponse =
          OAuthResponse.builder()
              .registrationToken(regToken)
              .name(null)
              .build();

      return new SignInKakaoResult(true, null, oauthResponse);
    }
  }
}
