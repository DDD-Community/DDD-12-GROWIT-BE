package com.growit.app.mission.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import com.growit.app.fake.mission.MissionFixture;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.MissionRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMissionUseCaseTest {

  @Mock private MissionRepository missionRepository;
  @InjectMocks private GetMissionUseCase useCase;

  @Test
  void given_user_has_today_missions_when_execute_then_return_list() {
    // given
    Mission mission = MissionFixture.defaultMission();
    given(missionRepository.findAllByUserIdAndToday(eq("user-1"), any(), any()))
        .willReturn(List.of(mission));

    // when
    List<Mission> result = useCase.execute("user-1");

    // then
    assertThat(result).isNotEmpty();
    assertThat(result.get(0).getId()).isEqualTo("mission-1");
  }

  @Test
  void given_user_has_no_today_missions_when_execute_then_return_empty_list() {
    // given
    given(missionRepository.findAllByUserIdAndToday(eq("user-1"), any(), any()))
        .willReturn(Collections.emptyList());

    // when
    List<Mission> result = useCase.execute("user-1");

    // then
    assertThat(result).isEmpty();
  }
}
