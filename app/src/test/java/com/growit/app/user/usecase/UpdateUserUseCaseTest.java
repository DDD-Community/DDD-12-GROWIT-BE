package com.growit.app.user.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.user.UserFixture;
import com.growit.app.resource.domain.jobrole.service.JobRoleQuery;
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

  @Mock private JobRoleQuery jobRoleQuery;

  @Mock private UserRepository userRepository;

  @InjectMocks private UpdateUserUseCase useCase;

  @Test
  void givenValidUserAndCommand_whenExecute_thenUpdateUserAndSave() {
    // given
    User user = UserFixture.defaultUser();
    UpdateUserCommand command = UserFixture.defaultUpdateUserCommand(user);
    doNothing().when(jobRoleQuery).checkJobRoleExist(command.jobRoleId());

    // when
    useCase.execute(command);
    // then
    verify(userRepository).saveUser(user);
    assertThat(user.getName()).isEqualTo(command.name());
    assertThat(user.getJobRoleId()).isEqualTo(command.jobRoleId());
    assertThat(user.getCareerYear()).isEqualTo(command.careerYear());
  }

  @Test
  void givenInValidUserAndCommand_whenExecute_thenUpdateUserAndSave() {
    // given
    UpdateUserCommand command = UserFixture.defaultUpdateUserCommand(UserFixture.defaultUser());

    doThrow(new BadRequestException("jobRoleId not exists"))
        .when(jobRoleQuery)
        .checkJobRoleExist(command.jobRoleId());

    assertThrows(BadRequestException.class, () -> useCase.execute(command));
  }
}
