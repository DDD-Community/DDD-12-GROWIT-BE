package com.growit.app.todo.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.service.ToDoService;
import com.growit.app.todo.domain.service.ToDoValidator;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateToDoUseCaseTest {

  private CreateToDoUseCase createToDoUseCase;

  @BeforeEach
  void setUp() {
    FakeToDoRepository fakeToDoRepository = new FakeToDoRepository();
    FakeGoalRepository fakeGoalRepository = new FakeGoalRepository();
    ToDoValidator toDoValidator = new ToDoService(fakeToDoRepository, fakeGoalRepository);
    createToDoUseCase = new CreateToDoUseCase(toDoValidator, fakeToDoRepository);

    // Goal 데이터 등록
    fakeGoalRepository.saveGoal(GoalFixture.defaultGoal());
  }

  @Test
  void givenValidCommand_whenExecute_thenReturnsToDoId() {
    Goal goal = GoalFixture.defaultGoal();
    FakeGoalRepository fakeGoalRepository = new FakeGoalRepository();
    fakeGoalRepository.saveGoal(goal);

    LocalDate validDate = goal.getDuration().startDate();
    String planId = goal.filterByDate(validDate).map(Plan::getId).orElseThrow();

    CreateToDoCommand command =
        new CreateToDoCommand("user-1", goal.getId(), planId, "테스트 내용", validDate);

    // When
    String toDoId = createToDoUseCase.execute(command);

    // Then
    assertNotNull(toDoId);
  }
}
