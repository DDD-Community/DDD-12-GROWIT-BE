package com.growit.app.user.domain.user.service;

import com.growit.app.common.exception.CustomException;
import com.growit.app.common.exception.ErrorCode;
import com.growit.app.user.domain.user.repository.UserRepository;
import com.growit.app.user.domain.user.vo.Email;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public void checkEmail(Email email) throws CustomException {
    if(userRepository.findByEmail(email).isPresent()){
      throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
    }
  }
}
