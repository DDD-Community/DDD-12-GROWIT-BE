package com.growit.app.mission.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.service.MissionSchedule;
import com.growit.app.mission.domain.vo.MissionType;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMissionUseCaseTest {

  @Mock private MissionSchedule missionSchedule;
  @Mock private ToDoRepository toDoRepository;
  @Mock private GoalRepository goalRepository;
  @Mock private RetrospectRepository retrospectRepository;

  @InjectMocks private GetMissionUseCase useCase;

  private LocalDate today;
  private DayOfWeek dayOfWeek;

  @BeforeEach
  void setUp() {
    today = LocalDate.now();
    dayOfWeek = today.getDayOfWeek();
  }

  @Test
  void given_user_has_missions_for_today_when_execute_then_return_mission_list() {
    // given
    String userId = "user-1";
    List<MissionType> missionTypes =
        List.of(MissionType.DAILY_TODO_WRITE, MissionType.DAILY_TODO_COMPLETE);
    List<ToDo> toDoList = List.of(createToDo("todo-1", true));

    given(missionSchedule.typesFor(dayOfWeek)).willReturn(missionTypes);
    given(toDoRepository.findByUserIdAndDate(userId, today)).willReturn(toDoList);
    given(goalRepository.findByUserIdAndGoalDuration(userId, today)).willReturn(Optional.empty());

    // when
    List<Mission> result = useCase.execute(userId);

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getType()).isEqualTo(MissionType.DAILY_TODO_WRITE);
    assertThat(result.get(1).getType()).isEqualTo(MissionType.DAILY_TODO_COMPLETE);
  }

  @Test
  void given_user_has_no_todos_when_execute_then_return_missions_with_finished_false() {
    // given
    String userId = "user-1";
    List<MissionType> missionTypes = List.of(MissionType.DAILY_TODO_WRITE);

    given(missionSchedule.typesFor(dayOfWeek)).willReturn(missionTypes);
    given(toDoRepository.findByUserIdAndDate(userId, today)).willReturn(Collections.emptyList());
    given(goalRepository.findByUserIdAndGoalDuration(userId, today)).willReturn(Optional.empty());

    // when
    List<Mission> result = useCase.execute(userId);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).isFinished()).isFalse();
  }

  @Test
  void given_user_has_weekly_goal_mission_when_execute_then_return_mission_with_goal_status() {
    // given
    String userId = "user-1";
    List<MissionType> missionTypes = List.of(MissionType.WEEKLY_GOAL_WRITE);
    Goal goal = mock(Goal.class);
    Plan plan = mock(Plan.class);

    given(missionSchedule.typesFor(dayOfWeek)).willReturn(missionTypes);
    given(toDoRepository.findByUserIdAndDate(userId, today)).willReturn(Collections.emptyList());
    given(goalRepository.findByUserIdAndGoalDuration(userId, today)).willReturn(Optional.of(goal));
    given(goal.getPlanByDate(today)).willReturn(plan);

    // when
    List<Mission> result = useCase.execute(userId);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getType()).isEqualTo(MissionType.WEEKLY_GOAL_WRITE);
  }

  @Test
  void
      given_user_has_weekly_retrospect_mission_when_execute_then_return_mission_with_retrospect_status() {
    // given
    String userId = "user-1";
    List<MissionType> missionTypes = List.of(MissionType.WEEKLY_RETROSPECT_WRITE);
    Goal goal = mock(Goal.class);
    Plan plan = mock(Plan.class);
    Retrospect retrospect = mock(Retrospect.class);

    given(missionSchedule.typesFor(dayOfWeek)).willReturn(missionTypes);
    given(toDoRepository.findByUserIdAndDate(userId, today)).willReturn(Collections.emptyList());
    given(goalRepository.findByUserIdAndGoalDuration(userId, today)).willReturn(Optional.of(goal));
    given(goal.getPlanByDate(today)).willReturn(plan);
    given(retrospectRepository.findByPlanId(plan.getId())).willReturn(Optional.of(retrospect));

    // when
    List<Mission> result = useCase.execute(userId);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getType()).isEqualTo(MissionType.WEEKLY_RETROSPECT_WRITE);
  }

  private ToDo createToDo(String id, boolean completed) {
    return ToDo.builder().id(id).content("테스트 투두").isCompleted(completed).build();
  }
}
