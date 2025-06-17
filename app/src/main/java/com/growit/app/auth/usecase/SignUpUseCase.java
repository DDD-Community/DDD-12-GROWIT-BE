package com.growit.app.auth.usecase;

import com.growit.app.auth.controller.dto.SignUpRequest;
import com.growit.app.auth.domain.dto.SignUpCommand;
import com.growit.app.common.exception.CustomException;
import com.growit.app.common.exception.ErrorCode;
import com.growit.app.user.domain.vo.CareerYear;
import com.growit.app.user.domain.entity.JobRoleEntity;
import com.growit.app.user.domain.repository.JobRoleRepository;
import com.growit.app.user.domain.service.UserService;
import com.growit.app.user.domain.vo.Email;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SignUpUseCase {
  private final UserService userService;
  private final JobRoleRepository jobRoleRepository;

  @Transactional
  public void execute(SignUpRequest request) {
    JobRoleEntity jobRole = jobRoleRepository.findById(request.getJobRoleId())
      .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT));

    SignUpCommand command = new SignUpCommand(
      new Email(request.getEmail()),
      request.getPassword(),
      request.getName(),
      jobRole,
      CareerYear.valueOf(request.getCareerYear().toUpperCase())
    );

    userService.signup(command);
  }
}
