package com.growit.app.user.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.token.UserTokenRepository;
import com.growit.app.user.domain.user.AppleTokenRevocationPort;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUseCaseTest {

  @Mock private UserTokenRepository userTokenRepository;
  @Mock private UserRepository userRepository;
  @Mock private AppleTokenRevocationPort appleTokenRevocationPort;

  @InjectMocks private DeleteUserUseCase deleteUserUseCase;

  @Test
  void givenUser_whenExecute_thenDeletesUserAndTokens() {
    // given
    User user = UserFixture.defaultUser();
    when(userTokenRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

    // when
    deleteUserUseCase.execute(user);

    // then
    verify(userTokenRepository).findByUserId(user.getId());
    verify(userRepository).saveUser(user);
  }
}
