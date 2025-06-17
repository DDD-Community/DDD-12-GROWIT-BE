package com.growit.app.user.infrastructure.service;

import com.growit.app.auth.domain.dto.SignUpCommand;
import com.growit.app.common.exception.CustomException;
import com.growit.app.common.exception.ErrorCode;
import com.growit.app.user.domain.entity.UserEntity;
import com.growit.app.user.domain.repository.UserRepository;
import com.growit.app.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void signup(SignUpCommand command) {
    if (userRepository.existsByEmail(command.email())) {
      throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
    }

    UserEntity userEntity = UserEntity.builder()
      .email(command.email())
      .password(passwordEncoder.encode(command.password()))
      .name(command.name())
      .jobRole(command.jobRole())
      .careerYear(command.careerYear())
      .refreshToken(null)
      .build();
    userRepository.save(userEntity);
  }
}
