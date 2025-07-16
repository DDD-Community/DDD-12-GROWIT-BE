package com.growit.app.user.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.domain.token.UserTokenRepository;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.UserTokenQuery;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.dto.ReIssueCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReissueUseCaseTest {

  @Mock private UserTokenRepository userTokenRepository;

  @Mock private UserTokenQuery userTokenQuery;

  @Mock private TokenGenerator tokenGenerator;

  @InjectMocks private ReissueUseCase reissueUseCase;

  @Test
  void givenValidRefreshToken_whenExecute_thenReturnNewToken() {
    // given
    ReIssueCommand command = UserFixture.defaultReIssueCommand();
    UserToken userToken = UserFixture.defaultUserToken();
    Token token = UserFixture.defaultToken();

    when(userTokenQuery.getUserToken(command.refreshToken())).thenReturn(userToken);
    when(tokenGenerator.reIssue(anyString())).thenReturn(token);

    // when
    Token result = reissueUseCase.execute(command);

    // then
    assertEquals(token, result);
  }

  @Test
  void givenInValidRefreshToken_whenExecute_thenThrowInvalidTokenException() {
    // given
    ReIssueCommand command = UserFixture.defaultReIssueCommand();

    when(userTokenQuery.getUserToken(command.refreshToken()))
        .thenThrow(new InvalidTokenException());

    assertThrows(InvalidTokenException.class, () -> reissueUseCase.execute(command));
  }
}
