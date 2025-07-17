package com.growit.app.user.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.UserTokenSaver;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.SignInCommand;
import com.growit.app.user.domain.user.service.UserQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignInUseCaseTest {

  @Mock private UserQuery userQuery;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private TokenGenerator tokenGenerator;

  @Mock private UserTokenSaver userTokenSaver;

  @InjectMocks private SignInUseCase signInUseCase;

  @Test
  void givenValidSignInCommand_whenExecute_thenReturnToken() {
    // given
    SignInCommand command = UserFixture.defaultSignInCommand();
    User user = UserFixture.defaultUser();
    Token token = UserFixture.defaultToken();

    when(userQuery.getUserByEmail(command.email())).thenReturn(user);
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(tokenGenerator.createToken(user)).thenReturn(token);

    // when
    Token result = signInUseCase.execute(command);

    // then
    assertEquals(token, result);
  }

  @Test
  void givenInvalidPassword_whenExecute_thenThrowNotFoundException() {
    // given
    SignInCommand command = UserFixture.defaultSignInCommand();
    User user = UserFixture.defaultUser();

    when(userQuery.getUserByEmail(command.email())).thenReturn(user);
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    // then
    assertThrows(NotFoundException.class, () -> signInUseCase.execute(command));
  }
}
