package com.growit.app.user.domain.user.repository;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;

import java.util.Optional;

// 얘는 기본적인 CRUD는 persistence, 구현체가 제어의 역전
public interface UserRepository {
  Optional<User> findByEmail(Email email);
  void save(User user);
}
