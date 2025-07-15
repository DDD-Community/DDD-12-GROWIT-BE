package com.growit.app.user.usecase;

import com.growit.app.resource.domain.jobrole.service.JobRoleQuery;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.UpdateUserCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateUserUseCase {
  private final JobRoleQuery jobRoleQuery;
  private final UserRepository userRepository;

  @Transactional
  public void execute(UpdateUserCommand command) {
    jobRoleQuery.checkJobRoleExist(command.jobRoleId());

    User user = command.user();
    user.updateByCommand(command);

    userRepository.saveUser(user);
  }
}
