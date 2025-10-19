package com.growit.app.user.domain.userstats.service;

import com.growit.app.user.domain.userstats.UserStats;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserStatsService implements ActiveUserQuery {

  @Override
  public Set<String> getActiveUserIds(List<UserStats> userStatsList, int diffDay) {
    final LocalDate today = LocalDate.now();

    return userStatsList.stream()
        .filter(userStats -> userStats.isActiveUser(today, diffDay))
        .map(UserStats::getUserId)
        .collect(Collectors.toSet());
  }
}
