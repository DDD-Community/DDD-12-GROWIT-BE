package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.goal.domain.goal.vo.GoalStatus;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.domain.goalretrospect.dto.CreateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.service.AIAnalysis;
import com.growit.app.retrospect.domain.goalretrospect.service.GoalRetrospectQuery;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import com.growit.app.retrospect.usecase.goalretrospect.CreateGoalRetrospectUseCase;
import com.growit.app.todo.domain.service.ToDoQuery;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateGoalRetrospectUseCaseTest {
  @Mock private GoalQuery goalQuery;
  @Mock private ToDoQuery toDoQuery;
  @Mock private AIAnalysis aiAnalysis;
  @Mock private GoalRetrospectRepository goalRetrospectRepository;
  @Mock private GoalRetrospectQuery goalRetrospectQuery;
  @Mock private RetrospectQuery retrospectQuery;

  @InjectMocks private CreateGoalRetrospectUseCase useCase;

  @Test
  void givenCommand_whenExecute_thenReturnsId() {
    // given
    Goal goal = GoalFixture.defaultGoal();
    goal.updateStatus(GoalStatus.ENDED);
    when(goalRetrospectQuery.isExistsByGoalId(goal.getId())).thenReturn(false);
    when(goalQuery.getMyGoal(goal.getId(), goal.getUserId())).thenReturn(goal);
    CreateGoalRetrospectCommand command =
        new CreateGoalRetrospectCommand(goal.getUserId(), goal.getId());

    when(toDoQuery.getToDosByGoalId(command.goalId())).thenReturn(List.of());
    when(aiAnalysis.generate(any())).thenReturn(new Analysis("", ""));
    final String id = useCase.execute(command);
    assertNotNull(id);
  }

  @Test
  void givenInvalidCommand_whenExecute_thenReturnsBadRequestException() {
    // given
    Goal goal = GoalFixture.defaultGoal();
    goal.updateStatus(GoalStatus.PROGRESS);
    when(goalRetrospectQuery.isExistsByGoalId(goal.getId())).thenReturn(false);
    when(goalQuery.getMyGoal(goal.getId(), goal.getUserId())).thenReturn(goal);
    CreateGoalRetrospectCommand command =
        new CreateGoalRetrospectCommand(goal.getUserId(), goal.getId());

    assertThrows(BadRequestException.class, () -> useCase.execute(command));
  }
}
