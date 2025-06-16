package com.growit.app.user.domain.service;

import com.growit.app.common.error.BadRequestError;
import com.growit.app.user.domain.UserRepository;
import com.growit.app.user.domain.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserValidator {
  private final UserRepository userRepository;

  @Override
  public void checkEmailExists(Email email) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new BadRequestError("이미 존재하는 이메일입니다.");
    }
  }
}
