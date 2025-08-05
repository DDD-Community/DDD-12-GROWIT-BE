package com.growit.app.goal.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.UpdateGoalCommand;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateGoalUseCaseTest {
  @Mock private GoalQuery goalQuery;
  @Mock private GoalRepository goalRepository;

  @InjectMocks private UpdateGoalUseCase useCase;

  @Test
  void givenValidCommand_whenExecute_thenReturnSuccess() {
    // given
    Goal goal = GoalFixture.defaultGoal();
    UpdateGoalCommand command =
        GoalFixture.defaultUpdateGoalCommand(goal.getName(), Collections.emptyList());

    when(goalQuery.getMyGoal(goal.getId(), goal.getUserId())).thenReturn(goal);

    // when
    useCase.execute(command);

    // then
    verify(goalQuery).getMyGoal(goal.getId(), goal.getUserId());
    verify(goalRepository).saveGoal(goal);
  }
}
