package com.growit.app.mission.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.mission.MissionFixture;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import com.growit.app.mission.domain.dto.UpdateMissionCommand;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateMissionUseCaseTest {

  @Mock private MissionRepository missionRepository;
  @InjectMocks private UpdateMissionUseCase useCase;

  @Test
  void given_existing_mission_when_update_then_mission_is_saved_with_new_status() {
    // given
    Mission mission = MissionFixture.defaultMission();
    given(missionRepository.findByIdAndUserId("mission-1", "user-1"))
        .willReturn(Optional.of(mission));

    UpdateMissionCommand command = new UpdateMissionCommand("mission-1", "user-1", true);

    // when
    useCase.execute(command);

    // then
    ArgumentCaptor<Mission> captor = ArgumentCaptor.forClass(Mission.class);
    verify(missionRepository).saveMission(captor.capture());

    Mission saved = captor.getValue();
    assertThat(saved.isFinished()).isTrue();
  }

  @Test
  void given_non_existing_mission_when_update_then_throw_NotFoundException() {
    // given
    given(missionRepository.findByIdAndUserId("not-exist", "user-1")).willReturn(Optional.empty());

    UpdateMissionCommand command = new UpdateMissionCommand("not-exist", "user-1", true);

    // when & then
    assertThatThrownBy(() -> useCase.execute(command)).isInstanceOf(NotFoundException.class);
  }
}
