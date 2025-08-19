package com.growit.app.mission.usecase;

import static com.growit.app.common.util.message.ErrorCode.MISSION_NOT_FOUND;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import com.growit.app.mission.domain.dto.UpdateMissionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateMissionUseCase {
  private final MissionRepository missionRepository;

  @Transactional
  public void execute(UpdateMissionCommand command) {
    Mission mission =
        missionRepository
            .findByIdAndUserId(command.id(), command.userId())
            .orElseThrow(() -> new NotFoundException(MISSION_NOT_FOUND.getCode()));
    mission.finished(command.finished());
    missionRepository.saveMission(mission);
  }
}
