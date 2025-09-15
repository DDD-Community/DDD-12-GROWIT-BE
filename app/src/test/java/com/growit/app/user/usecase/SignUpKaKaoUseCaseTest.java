package com.growit.app.user.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.growit.app.resource.domain.jobrole.service.JobRoleValidator;
import com.growit.app.user.domain.token.service.JwtClaimKeys;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.RequiredConsentCommand;
import com.growit.app.user.domain.user.dto.SignUpKaKaoCommand;
import com.growit.app.user.domain.user.service.UserValidator;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignUpKaKaoUseCaseTest {

  @Mock private UserRepository userRepository;
  @Mock private JobRoleValidator jobRoleValidator;
  @Mock private UserValidator userValidator;
  @Mock private TokenService tokenService;

  @InjectMocks private SignUpKaKaoUseCase signUpKaKaoUseCase;

  @Test
  void givenValidKakaoSignUpCommand_whenExecute_thenSaveUser() {
    // given
    SignUpKaKaoCommand signUpCommand =
        new SignUpKaKaoCommand("홍길동", "jobRoleId-1", CareerYear.JUNIOR, "dummy-registration-token");
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    Claims claims = mock(Claims.class);
    when(tokenService.parseClaims("dummy-registration-token")).thenReturn(claims);
    when(claims.get(JwtClaimKeys.PROVIDER, String.class)).thenReturn("kakao");
    when(claims.get(JwtClaimKeys.PROVIDER_ID, String.class)).thenReturn("12345");
    when(claims.get(JwtClaimKeys.EMAIL, String.class)).thenReturn("test@kakao.com");

    // when
    signUpKaKaoUseCase.execute(signUpCommand, requiredConsentCommand);

    // then
    verify(requiredConsentCommand).checkRequiredConsent();
    verify(tokenService).parseClaims("dummy-registration-token");
    verify(jobRoleValidator).checkJobRoleExist("jobRoleId-1");
    verify(userValidator).checkEmailExists(new Email("test@kakao.com"));
    verify(userValidator).checkOAuthExists(new OAuth("kakao", "12345"));

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).saveUser(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals("test@kakao.com", savedUser.getEmail().value());
    assertNull(savedUser.getPassword());
    assertEquals("홍길동", savedUser.getName());
    assertEquals("jobRoleId-1", savedUser.getJobRoleId());
    assertEquals(CareerYear.JUNIOR, savedUser.getCareerYear());
    assertEquals(1, savedUser.getOauthAccounts().size());
    assertEquals("kakao", savedUser.getOauthAccounts().get(0).provider());
    assertEquals("12345", savedUser.getOauthAccounts().get(0).providerId());
  }

  @Test
  void givenInvalidRegistrationToken_whenExecute_thenThrowException() {
    // given
    SignUpKaKaoCommand signUpCommand =
        new SignUpKaKaoCommand("홍길동", "jobRoleId-1", CareerYear.JUNIOR, "invalid-token");
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    when(tokenService.parseClaims("invalid-token"))
        .thenThrow(new RuntimeException("Invalid token"));

    // when & then
    assertThrows(
        RuntimeException.class,
        () -> signUpKaKaoUseCase.execute(signUpCommand, requiredConsentCommand));

    verify(requiredConsentCommand).checkRequiredConsent();
    verify(tokenService).parseClaims("invalid-token");
    verify(userRepository, never()).saveUser(any());
  }

  @Test
  void givenExistingEmail_whenExecute_thenThrowException() {
    // given
    SignUpKaKaoCommand signUpCommand =
        new SignUpKaKaoCommand("홍길동", "jobRoleId-1", CareerYear.JUNIOR, "dummy-registration-token");
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    Claims claims = mock(Claims.class);
    when(tokenService.parseClaims("dummy-registration-token")).thenReturn(claims);
    when(claims.get(JwtClaimKeys.PROVIDER, String.class)).thenReturn("kakao");
    when(claims.get(JwtClaimKeys.PROVIDER_ID, String.class)).thenReturn("12345");
    when(claims.get(JwtClaimKeys.EMAIL, String.class)).thenReturn("existing@kakao.com");

    doThrow(new RuntimeException("Email already exists"))
        .when(userValidator)
        .checkEmailExists(new Email("existing@kakao.com"));

    // when & then
    assertThrows(
        RuntimeException.class,
        () -> signUpKaKaoUseCase.execute(signUpCommand, requiredConsentCommand));

    verify(requiredConsentCommand).checkRequiredConsent();
    verify(tokenService).parseClaims("dummy-registration-token");
    verify(jobRoleValidator).checkJobRoleExist("jobRoleId-1");
    verify(userValidator).checkEmailExists(new Email("existing@kakao.com"));
    verify(userRepository, never()).saveUser(any());
  }

  @Test
  void givenExistingOAuth_whenExecute_thenThrowException() {
    // given
    SignUpKaKaoCommand signUpCommand =
        new SignUpKaKaoCommand("홍길동", "jobRoleId-1", CareerYear.JUNIOR, "dummy-registration-token");
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    Claims claims = mock(Claims.class);
    when(tokenService.parseClaims("dummy-registration-token")).thenReturn(claims);
    when(claims.get(JwtClaimKeys.PROVIDER, String.class)).thenReturn("kakao");
    when(claims.get(JwtClaimKeys.PROVIDER_ID, String.class)).thenReturn("12345");
    when(claims.get(JwtClaimKeys.EMAIL, String.class)).thenReturn("test@kakao.com");

    doThrow(new RuntimeException("OAuth account already exists"))
        .when(userValidator)
        .checkOAuthExists(new OAuth("kakao", "12345"));

    // when & then
    assertThrows(
        RuntimeException.class,
        () -> signUpKaKaoUseCase.execute(signUpCommand, requiredConsentCommand));

    verify(requiredConsentCommand).checkRequiredConsent();
    verify(tokenService).parseClaims("dummy-registration-token");
    verify(jobRoleValidator).checkJobRoleExist("jobRoleId-1");
    verify(userValidator).checkEmailExists(new Email("test@kakao.com"));
    verify(userValidator).checkOAuthExists(new OAuth("kakao", "12345"));
    verify(userRepository, never()).saveUser(any());
  }

  @Test
  void givenInvalidJobRole_whenExecute_thenThrowException() {
    // given
    SignUpKaKaoCommand signUpCommand =
        new SignUpKaKaoCommand(
            "홍길동", "invalidJobRole", CareerYear.JUNIOR, "dummy-registration-token");
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    Claims claims = mock(Claims.class);
    when(tokenService.parseClaims("dummy-registration-token")).thenReturn(claims);
    when(claims.get(JwtClaimKeys.PROVIDER, String.class)).thenReturn("kakao");
    when(claims.get(JwtClaimKeys.PROVIDER_ID, String.class)).thenReturn("12345");
    when(claims.get(JwtClaimKeys.EMAIL, String.class)).thenReturn("test@kakao.com");

    doThrow(new RuntimeException("Invalid job role"))
        .when(jobRoleValidator)
        .checkJobRoleExist("invalidJobRole");

    // when & then
    assertThrows(
        RuntimeException.class,
        () -> signUpKaKaoUseCase.execute(signUpCommand, requiredConsentCommand));

    verify(requiredConsentCommand).checkRequiredConsent();
    verify(tokenService).parseClaims("dummy-registration-token");
    verify(jobRoleValidator).checkJobRoleExist("invalidJobRole");
    verify(userRepository, never()).saveUser(any());
  }

  @Test
  void givenInvalidConsent_whenExecute_thenThrowException() {
    // given
    SignUpKaKaoCommand signUpCommand =
        new SignUpKaKaoCommand("홍길동", "jobRoleId-1", CareerYear.JUNIOR, "dummy-registration-token");
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    doThrow(new RuntimeException("Required consent not provided"))
        .when(requiredConsentCommand)
        .checkRequiredConsent();

    // when & then
    assertThrows(
        RuntimeException.class,
        () -> signUpKaKaoUseCase.execute(signUpCommand, requiredConsentCommand));

    verify(requiredConsentCommand).checkRequiredConsent();
    verify(tokenService, never()).parseClaims(anyString());
    verify(jobRoleValidator, never()).checkJobRoleExist(anyString());
    verify(userValidator, never()).checkEmailExists(any());
    verify(userRepository, never()).saveUser(any());
  }
}
