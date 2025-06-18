package com.growit.app.user.controller.auth.usecase;

import com.growit.app.user.controller.auth.dto.SignUpRequest;
import com.growit.app.user.domain.auth.dto.SignUpCommand;
import com.growit.app.common.exception.CustomException;
import com.growit.app.common.exception.ErrorCode;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.repository.UserRepository;
import com.growit.app.user.domain.user.service.UserService;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SignUpUseCase {
  private final UserRepository userRepository;
  private final JobRoleRepository jobRoleRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Transactional
  public void execute(SignUpRequest request) throws CustomException {
    JobRole jobRole = jobRoleRepository.findById(request.getJobRoleId())
      .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT));

    SignUpCommand command = new SignUpCommand(
      new Email(request.getEmail()),
      request.getPassword(),
      request.getName(),
      jobRole.getId(),
      CareerYear.valueOf(request.getCareerYear().toUpperCase())
    );

    userService.checkEmail(command.email());

    User user = User.builder()
      .email(command.email())
      .password(passwordEncoder.encode(command.password()))
      .name(command.name())
      .jobRoleId(command.jobRoleId())
      .careerYear(command.careerYear())
      .build();

    userRepository.save(user);
  }
}
