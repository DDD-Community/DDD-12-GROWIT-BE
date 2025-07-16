package com.growit.app.user.usecase;

import com.growit.app.resource.domain.jobrole.service.JobRoleValidator;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.UpdateUserCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateUserUseCase {
  private final JobRoleValidator jobRoleValidator;
  private final UserRepository userRepository;

  @Transactional
  public void execute(UpdateUserCommand command) {
    jobRoleValidator.checkJobRoleExist(command.jobRoleId());

    User user = command.user();
    user.updateByCommand(command);

    userRepository.saveUser(user);
  }
}
