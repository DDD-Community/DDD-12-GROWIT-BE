package com.growit.app.user.usecase;

import com.growit.app.common.exception.BaseException;
import com.growit.app.user.domain.jobrole.service.JobRoleService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpUseCase {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JobRoleService jobRoleService;
  private final UserService userService;

  @Transactional
  public void execute(SignUpCommand command) throws BaseException {

    jobRoleService.checkJobRoleExist(command.jobRoleId());
    userService.checkEmailExists(command.email());

    final SignUpCommand encodePassword =
        command.encodePassword(passwordEncoder.encode(command.password()));
    final User user = User.from(encodePassword);

    userRepository.saveUser(user);
  }
}
