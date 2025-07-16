package com.growit.app.user.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.token.UserTokenRepository;
import com.growit.app.user.domain.token.service.UserTokenQuery;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;
import com.growit.app.user.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LogoutUseCaseTest {

  @Mock private UserTokenRepository userTokenRepository;

  @Mock private UserTokenQuery userTokenQuery;

  @InjectMocks private LogoutUseCase logoutUseCase;

  @Test
  void givenUserTokenNotExists_whenExecuteLogout_thenThrowsInvalidUserTokenException() {
    // given
    User user = UserFixture.defaultUser();
    when(userTokenQuery.getUserTokenByUserId(user.getId())).thenThrow(InvalidTokenException.class);

    // when & then
    assertThrows(InvalidTokenException.class, () -> logoutUseCase.execute(user));
  }
}
