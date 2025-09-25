package com.growit.app.user.usecase;

import com.growit.app.resource.domain.jobrole.JobRole;
import com.growit.app.resource.domain.jobrole.service.JobRoleQuery;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.UserDto;
import com.growit.app.user.domain.userstats.UserStats;
import com.growit.app.user.domain.userstats.UserStatsRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserUseCase {
  private final JobRoleQuery jobRoleQuery;
  private final UserStatsRepository userStatsRepository;

  @Transactional
  public UserDto execute(User user) {
    final JobRole jobRole = jobRoleQuery.getJobRoleById(user.getJobRoleId());
    final UserStats userStats = userStatsRepository.findByUserId(user.getId());
    userStats.updateLastSeenDate(LocalDate.now());
    userStatsRepository.save(userStats);

    return new UserDto(user, jobRole);
  }
}
