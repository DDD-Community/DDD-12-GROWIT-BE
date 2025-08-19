package com.growit.app.mission.usecase;

import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMissionUseCase {
  private final MissionRepository missionRepository;

  public void execute(CreateMissionCommand command) {
    Mission mission = Mission.from(command);
    missionRepository.saveMission(mission);
  }
}
