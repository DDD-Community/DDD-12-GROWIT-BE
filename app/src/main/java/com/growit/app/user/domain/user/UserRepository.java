package com.growit.app.user.domain.user;

public interface UserRepository {
  User findById(String id);
}
