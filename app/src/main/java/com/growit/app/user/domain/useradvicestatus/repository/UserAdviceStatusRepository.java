package com.growit.app.user.domain.useradvicestatus.repository;

import com.growit.app.user.domain.useradvicestatus.UserAdviceStatus;
import java.util.Optional;

public interface UserAdviceStatusRepository {
  Optional<UserAdviceStatus> findByUserId(String userId);

  void save(UserAdviceStatus userAdviceStatus);
}
