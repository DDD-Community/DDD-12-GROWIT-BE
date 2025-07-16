package com.growit.app.user.domain.user.service;

import static com.growit.app.common.util.message.ErrorCode.USER_SIGN_IN_FAILED;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserValidator, UserQuery {

  private final UserRepository userRepository;

  @Override
  public void checkEmailExists(Email email) throws AlreadyExistEmailException {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new AlreadyExistEmailException();
    }
  }

  @Override
  public User getUserByEmail(Email email) throws NotFoundException {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new NotFoundException(USER_SIGN_IN_FAILED.getCode()));
  }
}
