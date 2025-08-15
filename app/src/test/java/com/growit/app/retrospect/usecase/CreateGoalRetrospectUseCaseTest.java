package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.domain.goalretrospect.dto.CreateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.service.AIAnalysis;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
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

  @InjectMocks private CreateGoalRetrospectUseCase useCase;

  @Test
  void givenCommand_whenExecute_thenReturnsId() {
    // given
    Goal goal = GoalFixture.defaultGoal();
    when(goalQuery.getMyGoal(goal.getId(), goal.getUserId())).thenReturn(goal);
    CreateGoalRetrospectCommand command =
        new CreateGoalRetrospectCommand(goal.getUserId(), goal.getId());

    when(toDoQuery.getToDosByGoalId(command.goalId())).thenReturn(List.of());
    when(aiAnalysis.generate(goal, List.of())).thenReturn(new Analysis("", ""));
    final String id = useCase.execute(command);
    assertNotNull(id);
  }
}
