package com.growit.app.user.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.RequiredConsentCommand;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.service.UserValidator;
import com.growit.app.user.domain.user.vo.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignUpUseCaseTest {

  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserValidator userValidator;

  @InjectMocks private SignUpUseCase signUpUseCase;

  @Test
  void givenValidSignUpCommand_whenExecute_thenSaveUser() {
    // given
    SignUpCommand signUpCommand =
        new SignUpCommand(
            new Email("test@example.com"),
            "password123",
            "홍길동",
            null,
            null);
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

    // when
    signUpUseCase.execute(signUpCommand, requiredConsentCommand);

    // then
    verify(requiredConsentCommand).checkRequiredConsent();
    verify(userValidator).checkEmailExists(new Email("test@example.com"));

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).saveUser(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals("test@example.com", savedUser.getEmail().value());
    assertEquals("encodedPassword", savedUser.getPassword());
    assertEquals("홍길동", savedUser.getName());
  }


  @Test
  void givenExistingEmail_whenExecute_thenThrowException() {
    // given
    SignUpCommand signUpCommand =
        new SignUpCommand(
            new Email("existing@example.com"),
            "password123",
            "홍길동",
            null,
            null);
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    doThrow(new RuntimeException("Email already exists"))
        .when(userValidator)
        .checkEmailExists(new Email("existing@example.com"));

    // when & then
    assertThrows(
        RuntimeException.class, () -> signUpUseCase.execute(signUpCommand, requiredConsentCommand));

    verify(requiredConsentCommand).checkRequiredConsent();
    verify(userValidator).checkEmailExists(new Email("existing@example.com"));
    verify(userRepository, never()).saveUser(any());
  }

  @Test
  void givenInvalidConsent_whenExecute_thenThrowException() {
    // given
    SignUpCommand signUpCommand =
        new SignUpCommand(
            new Email("test@example.com"),
            "password123",
            "홍긘동",
            null,
            null);
    RequiredConsentCommand requiredConsentCommand = mock(RequiredConsentCommand.class);

    doThrow(new RuntimeException("Required consent not provided"))
        .when(requiredConsentCommand)
        .checkRequiredConsent();

    // when & then
    assertThrows(
        RuntimeException.class, () -> signUpUseCase.execute(signUpCommand, requiredConsentCommand));

    verify(requiredConsentCommand).checkRequiredConsent();
    verify(userValidator, never()).checkEmailExists(any());
    verify(userRepository, never()).saveUser(any());
  }
}
