package com.growit.app.user.domain.user.service;

import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserValidator {
  private final UserRepository userRepository;

  @Override
  public void checkEmailExists(Email email) throws AlreadyExistEmailException {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new AlreadyExistEmailException();
    }
  }
}
