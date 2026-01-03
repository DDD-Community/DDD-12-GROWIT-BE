package com.growit.app.user.domain.useradvicestatus.service;

import com.growit.app.user.domain.useradvicestatus.UserAdviceStatus;
import com.growit.app.user.domain.useradvicestatus.repository.UserAdviceStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAdviceStatusService {

  private final UserAdviceStatusRepository repository;

  public void completeGoalOnboarding(String userId) {
    UserAdviceStatus status = new UserAdviceStatus(userId, true);
    repository.save(status);
  }

  public boolean isGoalOnboardingCompleted(String userId, Boolean isOnboarding) {
    if (Boolean.TRUE.equals(isOnboarding)) {
      return true;
    }
    return repository
        .findByUserId(userId)
        .map(UserAdviceStatus::isGoalOnboardingCompleted)
        .orElse(false);
  }
}
