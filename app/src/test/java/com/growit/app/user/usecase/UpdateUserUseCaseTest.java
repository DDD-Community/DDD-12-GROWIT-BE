package com.growit.app.user.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.growit.app.fake.user.UserFixture;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.UpdateUserCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UpdateUserUseCase useCase;

  @Test
  void givenValidUserAndCommand_whenExecute_thenUpdateUserAndSave() {
    // given
    User user = UserFixture.defaultUser();
    UpdateUserCommand command = UserFixture.defaultUpdateUserCommand(user);

    // when
    useCase.execute(command);
    // then
    verify(userRepository).saveUser(user);
    assertThat(user.getName()).isEqualTo(command.name());
  }
}
