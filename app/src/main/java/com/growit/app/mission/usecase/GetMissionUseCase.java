package com.growit.app.mission.usecase;

import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetMissionUseCase {
  private final MissionRepository missionRepository;

  @Transactional(readOnly = true)
  public List<Mission> execute(String userId) {
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

    List<Mission> missions =
        missionRepository.findAllByUserIdAndToday(userId, startOfDay, endOfDay);
    if (missions.isEmpty()) return Collections.emptyList();
    return missions;
  }
}
