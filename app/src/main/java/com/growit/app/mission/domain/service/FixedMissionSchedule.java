package com.growit.app.mission.domain.service;

import com.growit.app.mission.domain.vo.MissionType;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class FixedMissionSchedule implements MissionSchedule {

  private static final List<MissionType> DAILY =
      List.of(MissionType.DAILY_TODO_WRITE, MissionType.DAILY_TODO_COMPLETE);

  private static final Map<DayOfWeek, List<MissionType>> EXTRA =
      Map.of(
          DayOfWeek.MONDAY, List.of(MissionType.WEEKLY_GOAL_WRITE),
          DayOfWeek.SUNDAY, List.of(MissionType.WEEKLY_RETROSPECT_WRITE));

  @Override
  public List<MissionType> typesFor(DayOfWeek day) {
    List<MissionType> extras = EXTRA.getOrDefault(day, List.of());
    return Stream.concat(DAILY.stream(), extras.stream()).toList();
  }
}
