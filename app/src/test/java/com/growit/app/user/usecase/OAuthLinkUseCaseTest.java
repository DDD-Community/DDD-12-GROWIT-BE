package com.growit.app.user.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.OAuthCommand;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OAuthLinkUseCaseTest {

  @InjectMocks private OAuthLinkUseCase oAuthLinkUseCase;

  @Mock private UserRepository userRepository;

  private OAuthCommand command;
  private User pureEmailUser;
  private User existingOAuthUser;

  @BeforeEach
  void setUp() {
    command = new OAuthCommand("test@example.com", "kakao", "kakao123");

    // 순수 이메일 가입자 (OAuth 계정 없음)
    pureEmailUser =
        User.builder()
            .id("user1")
            .email(new Email("test@example.com"))
            .password("password")
            .name("테스트유저")
            .isOnboarding(false)
            .isDeleted(false)
            .oauthAccounts(new ArrayList<>()) // 변경 가능한 빈 리스트
            .build();

    // 기존 OAuth 사용자
    existingOAuthUser =
        User.builder()
            .id("user2")
            .email(new Email("test@example.com"))
            .password(null)
            .name("카카오유저")
            .isOnboarding(false)
            .isDeleted(false)
            .oauthAccounts(new ArrayList<>(List.of(new OAuth("kakao", "kakao123"))))
            .build();
  }

  @Test
  @DisplayName("순수 이메일 가입자가 있으면 OAuth 계정을 연결하고 사용자를 반환한다")
  void should_LinkOAuthAndReturnUser_When_PureEmailUserExists() {
    // given
    given(userRepository.findByEmail(new Email(command.email())))
        .willReturn(Optional.of(pureEmailUser));

    // when
    Optional<User> result = oAuthLinkUseCase.execute(command);

    // then
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(pureEmailUser);
    assertThat(result.get().hasAnyOAuth()).isTrue();
    assertThat(result.get().hasProvider(command.provider())).isTrue();

    // 사용자 저장 호출 확인
    then(userRepository).should().saveUser(eq(pureEmailUser));
  }

  @Test
  @DisplayName("이미 다른 OAuth 계정을 가진 사용자는 예외가 발생한다")
  void should_ThrowException_When_UserAlreadyHasAnotherOAuth() {
    // given
    User userWithOAuth =
        User.builder()
            .id("user3")
            .email(new Email("test@example.com"))
            .password("password")
            .name("기존OAuth유저")
            .isOnboarding(false)
            .isDeleted(false)
            .oauthAccounts(
                new ArrayList<>(List.of(new OAuth("google", "google123")))) // 다른 OAuth 계정 보유
            .build();

    given(userRepository.findByEmail(new Email(command.email())))
        .willReturn(Optional.of(userWithOAuth));

    // when & then
    assertThatThrownBy(() -> oAuthLinkUseCase.execute(command))
        .isInstanceOf(BadRequestException.class)
        .hasMessage("이미 google 계정으로 가입된 이메일입니다.");

    // 저장은 하지 않음
    then(userRepository).should(never()).saveUser(any(User.class));
  }

  @Test
  @DisplayName("해당 이메일의 사용자가 없으면 신규 사용자로 처리한다")
  void should_ReturnEmpty_When_NoUserWithEmail() {
    // given
    given(userRepository.findByEmail(new Email(command.email()))).willReturn(Optional.empty());

    // when
    Optional<User> result = oAuthLinkUseCase.execute(command);

    // then
    assertThat(result).isEmpty();

    // 저장은 하지 않음
    then(userRepository).should(never()).saveUser(any(User.class));
  }

  @Test
  @DisplayName("이메일이 null인 경우 예외가 발생한다")
  void should_ThrowException_When_EmailIsNull() {
    // given
    OAuthCommand commandWithNullEmail = new OAuthCommand(null, "kakao", "kakao123");

    // when & then
    assertThatThrownBy(() -> oAuthLinkUseCase.execute(commandWithNullEmail))
        .isInstanceOf(NullPointerException.class);

    // repository 호출은 하지 않음
    then(userRepository).should(never()).findByEmail(any(Email.class));
    then(userRepository).should(never()).saveUser(any(User.class));
  }

  @Test
  @DisplayName("순수 이메일 사용자에게 OAuth 연결 후 올바른 상태가 되는지 확인")
  void should_HaveCorrectState_After_OAuthLinking() {
    // given
    given(userRepository.findByEmail(new Email(command.email())))
        .willReturn(Optional.of(pureEmailUser));

    // when
    Optional<User> result = oAuthLinkUseCase.execute(command);

    // then
    assertThat(result).isPresent();
    User linkedUser = result.get();

    // OAuth 연결 상태 확인
    assertThat(linkedUser.hasAnyOAuth()).isTrue();
    assertThat(linkedUser.hasProvider("kakao")).isTrue();

    // 기존 정보는 유지
    assertThat(linkedUser.getEmail().value()).isEqualTo("test@example.com");
    assertThat(linkedUser.getName()).isEqualTo("테스트유저");
    assertThat(linkedUser.getPassword()).isEqualTo("password");

    then(userRepository).should().saveUser(linkedUser);
  }
}
