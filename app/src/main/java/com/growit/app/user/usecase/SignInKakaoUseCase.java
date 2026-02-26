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
    // 1. Verify id_token and extract attributes (also validates nonce if implemented in validator)
    Map<String, Object> attributes =
        kakaoIdTokenValidator.parseAndVerifyIdToken(command.idToken(), command.nonce());
    String sub = (String) attributes.get(KakaoKeys.SUB);

    // 이메일은 id_token 자체 클레임에 없을 수도 있고, 카카오 스코프 동의 여부에 따라 다름
    // 애플 로그인과 호환을 위해 email을 추출 (필요 시 수정)
    String email = (String) attributes.get(KakaoKeys.EMAIL);

    // 2. Find existing user or link new oauth entry
    OAuthCommand oauthCommand =
        new OAuthCommand(email, KakaoKeys.PROVIDER_NAME, sub, command.refreshToken());
    Optional<User> existingUser = oAuthLinkUseCase.execute(oauthCommand);

    if (existingUser.isPresent()) {
      // Existing member -> Login
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
      // New user -> Pending Signup -> Issue registration token
      String regToken =
          tokenService.createRegistrationToken(
              KakaoKeys.PROVIDER_NAME, sub, email, command.refreshToken());

      OAuthResponse oauthResponse =
          OAuthResponse.builder()
              .registrationToken(regToken)
              .name(null) // 카카오 SDK OIDC는 기본적으로 이름을 주지 않을 수 있음
              .build();

      return new SignInKakaoResult(true, null, oauthResponse);
    }
  }
}
