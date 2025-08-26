package com.growit.app.mission.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.growit.app.fake.user.UserFixture;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import com.growit.app.mission.domain.dto.CreateMissionCommand;
import com.growit.app.user.domain.user.UserRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateMissionUseCaseTest {

  @Mock private MissionRepository missionRepository;
  @Mock private UserRepository userRepository;
  @InjectMocks private CreateMissionUseCase useCase;

  @Test
  void given_user_has_no_today_mission_when_execute_then_mission_created() {
    // given
    CreateMissionCommand command = new CreateMissionCommand(false, "finished todo");

    given(userRepository.findAll(any()))
        .willReturn(
            new org.springframework.data.domain.PageImpl<>(List.of(UserFixture.defaultUser())));

    given(missionRepository.findUserIdsHavingContentToday(anyList(), anyString(), any(), any()))
        .willReturn(Collections.emptyList());

    // when
    useCase.execute(command);

    // then
    ArgumentCaptor<List<Mission>> captor = ArgumentCaptor.forClass(List.class);
    verify(missionRepository, times(1)).saveAll(captor.capture());

    List<Mission> savedMissions = captor.getValue();
    assertThat(savedMissions).hasSize(1);
    assertThat(savedMissions.get(0).getContent()).isEqualTo("finished todo");
    assertThat(savedMissions.get(0).isFinished()).isFalse();
  }

  @Test
  void given_user_already_has_today_mission_when_execute_then_skip_creation() {
    // given
    CreateMissionCommand command = new CreateMissionCommand(false, "finished todo");

    given(userRepository.findAll(any()))
        .willReturn(
            new org.springframework.data.domain.PageImpl<>(List.of(UserFixture.defaultUser())));

    given(
            missionRepository.findUserIdsHavingContentToday(
                anyList(), eq("finished todo"), any(), any()))
        .willReturn(List.of("user-1"));

    // when
    useCase.execute(command);

    // then
    verify(missionRepository, never()).saveAll(any());
  }
}
