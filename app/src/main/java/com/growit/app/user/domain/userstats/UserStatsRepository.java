package com.growit.app.user.domain.userstats;

import java.util.List;

public interface UserStatsRepository {
  UserStats findByUserId(String userId);

  List<UserStats> findByUserIds(List<String> userIds);

  void save(UserStats userStats);
}
