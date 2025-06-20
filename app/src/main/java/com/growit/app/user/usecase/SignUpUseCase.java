package com.growit.app.user.usecase;

import com.growit.app.common.exception.BaseException;
import com.growit.app.user.controller.dto.request.SignUpRequest;
import com.growit.app.user.domain.jobrole.service.JobRoleService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.SignUpCommand;
import com.growit.app.user.domain.user.service.UserService;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
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
  public void execute(SignUpRequest request) throws BaseException {
    SignUpCommand command =
        new SignUpCommand(
            new Email(request.getEmail()),
            request.getPassword(),
            request.getName(),
            request.getJobRoleId(),
            CareerYear.valueOf(request.getCareerYear().toUpperCase()));

    jobRoleService.checkJobRoleExist(command.jobRoleId());
    userService.checkEmailExists(command.email());

    User user =
        User.builder()
            .email(command.email())
            .password(passwordEncoder.encode(command.password()))
            .name(command.name())
            .jobRoleId(command.jobRoleId())
            .careerYear(command.careerYear())
            .build();

    userRepository.saveUser(user);
  }
}
