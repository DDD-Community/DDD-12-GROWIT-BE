package com.growit.app.user.domain.user.service;

import com.growit.app.common.exception.CustomException;
import com.growit.app.user.domain.user.vo.Email;

public interface UserService {
  void checkEmail(Email email) throws CustomException;
}
