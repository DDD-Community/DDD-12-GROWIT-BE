package com.growit.app.user.domain.user.service;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;

public interface UserQuery {
  User getUserByEmail(Email email) throws NotFoundException;
}
