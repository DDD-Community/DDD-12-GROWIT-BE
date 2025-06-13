package com.growit.app.user.domain;

public interface UserRepository {
  User findById(String id);
}
