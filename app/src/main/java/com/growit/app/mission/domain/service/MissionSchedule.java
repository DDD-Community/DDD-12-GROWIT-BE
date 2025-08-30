package com.growit.app.mission.domain.service;

import com.growit.app.mission.domain.vo.MissionType;
import java.time.DayOfWeek;
import java.util.List;

public interface MissionSchedule {
  List<MissionType> typesFor(DayOfWeek day);
}
