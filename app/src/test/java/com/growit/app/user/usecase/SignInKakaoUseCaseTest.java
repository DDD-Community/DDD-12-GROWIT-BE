package com.growit.app.user.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growit.app.common.config.oauth.KakaoKeys;
import com.growit.app.user.domain.token.service.KakaoIdTokenValidator;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.service.UserTokenSaver;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.OAuthCommand;
import com.growit.app.user.domain.user.dto.SignInKakaoCommand;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignInKakaoUseCaseTest {

  @Mock private KakaoIdTokenValidator kakaoIdTokenValidator;
  @Mock private OAuthLinkUseCase oAuthLinkUseCase;
  @Mock private TokenGenerator tokenGenerator;
  @Mock private UserTokenSaver userTokenSaver;
  @Mock private TokenService tokenService;

  private SignInKakaoUseCase signInKakaoUseCase;

  @BeforeEach
  void setUp() {
    signInKakaoUseCase =
        new SignInKakaoUseCase(
            kakaoIdTokenValidator, oAuthLinkUseCase, tokenGenerator, userTokenSaver, tokenService);
  }

  @Test
  @DisplayName("기존 가입 유저의 카카오 로그인 성공 시 TokenResponse를 반환한다")
  void testSignInExistingUser() {
    // given
    String idToken = "valid.id.token";
    String nonce = "test-nonce";
    String refreshToken = "kakao-refresh-token";
    SignInKakaoCommand command = new SignInKakaoCommand(idToken, refreshToken, nonce);

    Map<String, Object> mockAttributes = new HashMap<>();
    mockAttributes.put(KakaoKeys.SUB, "sub-1234");
    mockAttributes.put(KakaoKeys.EMAIL, "test@kakao.com");

    when(kakaoIdTokenValidator.parseAndVerifyIdToken(idToken, nonce)).thenReturn(mockAttributes);

    User mockUser = mock(User.class);
    when(mockUser.getId()).thenReturn("user-1");
    when(oAuthLinkUseCase.execute(any(OAuthCommand.class))).thenReturn(Optional.of(mockUser));

    Token mockToken = new Token("access-token", "refresh-token");
    when(tokenGenerator.createToken(any())).thenReturn(mockToken);

    // when
    SignInKakaoResult result = signInKakaoUseCase.execute(command);

    // then
    assertThat(result.isPendingSignup()).isFalse();
    assertThat(result.tokenResponse()).isNotNull();
    assertThat(result.tokenResponse().getAccessToken()).isEqualTo("access-token");
    assertThat(result.tokenResponse().getRefreshToken()).isEqualTo("refresh-token");
    assertThat(result.oauthResponse()).isNull();

    verify(userTokenSaver).saveUserToken(eq("user-1"), eq(mockToken));
  }

  @Test
  @DisplayName("신규 유저의 카카오 로그인 시 RegistrationToken을 반환한다")
  void testSignInNewUser() {
    // given
    String idToken = "valid.id.token";
    String nonce = "test-nonce";
    String refreshToken = "kakao-refresh-token";
    SignInKakaoCommand command = new SignInKakaoCommand(idToken, refreshToken, nonce);

    Map<String, Object> mockAttributes = new HashMap<>();
    mockAttributes.put(KakaoKeys.SUB, "sub-5678");
    mockAttributes.put(KakaoKeys.EMAIL, "new@kakao.com");

    when(kakaoIdTokenValidator.parseAndVerifyIdToken(idToken, nonce)).thenReturn(mockAttributes);
    when(oAuthLinkUseCase.execute(any(OAuthCommand.class))).thenReturn(Optional.empty());

    String regToken = "registration-token-123";
    when(tokenService.createRegistrationToken(
            KakaoKeys.PROVIDER_NAME, "sub-5678", "new@kakao.com", refreshToken))
        .thenReturn(regToken);

    // when
    SignInKakaoResult result = signInKakaoUseCase.execute(command);

    // then
    assertThat(result.isPendingSignup()).isTrue();
    assertThat(result.oauthResponse()).isNotNull();
    assertThat(result.oauthResponse().getRegistrationToken()).isEqualTo(regToken);
    assertThat(result.tokenResponse()).isNull();
  }

  @Test
  @DisplayName("이메일 정보가 없을 경우 sub 기반의 더미 이메일을 생성하여 처리한다")
  void testSignInWithoutEmail() {
    // given
    String idToken = "valid.id.token";
    String nonce = "test-nonce";
    SignInKakaoCommand command = new SignInKakaoCommand(idToken, null, nonce);

    Map<String, Object> mockAttributes = new HashMap<>();
    mockAttributes.put(KakaoKeys.SUB, "sub-9999");
    // 이메일 없음

    when(kakaoIdTokenValidator.parseAndVerifyIdToken(idToken, nonce)).thenReturn(mockAttributes);

    User mockUser = mock(User.class);
    when(oAuthLinkUseCase.execute(argThat(cmd -> cmd.email().equals("sub-9999@kakao.com"))))
        .thenReturn(Optional.of(mockUser));

    when(tokenGenerator.createToken(any(User.class))).thenReturn(new Token("access", "refresh"));

    // when
    signInKakaoUseCase.execute(command);

    // then
    verify(oAuthLinkUseCase).execute(argThat(cmd -> "sub-9999@kakao.com".equals(cmd.email())));
  }
}
