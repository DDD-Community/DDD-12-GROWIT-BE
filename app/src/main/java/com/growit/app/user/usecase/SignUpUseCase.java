package com.growit.app.user.usecase;

import com.growit.app.user.domain.jobrole.service.JobRoleValidator;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.service.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignUpUseCase {

  private final JobRoleValidator jobRoleValidator;
  private final UserRepository userRepository;
  private final UserValidator userValidator;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void execute(SignUpCommand command) {
    jobRoleValidator.checkJobRoleExists(command.jobRoleId());
    userValidator.checkEmailExists(command.email());

    SignUpCommand encodedCommand =
        command.withEncodedPassword(passwordEncoder.encode(command.password()));
    User user = User.from(encodedCommand);
    userRepository.saveUser(user);
  }
}
