package com.growit.app.todo.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.FakeToDoQuery;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.FakeToDoValidator;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.service.ToDoValidator;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateToDoUseCaseTest {

  private UpdateToDoUseCase updateToDoUseCase;
  private FakeToDoRepository fakeToDoRepository;
  private ToDo toDo;

  @BeforeEach
  void setUp() {
    FakeGoalRepository fakeGoalRepository = new FakeGoalRepository();
    fakeToDoRepository = new FakeToDoRepository();
    FakeToDoQuery toDoQuery = new FakeToDoQuery(fakeToDoRepository);
    ToDoValidator toDoValidator = new FakeToDoValidator();
    updateToDoUseCase =
        new UpdateToDoUseCase(toDoQuery, toDoValidator, fakeToDoRepository, fakeGoalRepository);

    Goal goal = GoalFixture.defaultGoal();
    fakeGoalRepository.saveGoal(GoalFixture.defaultGoal());

    LocalDate today = LocalDate.now();
    String planId = goal.filterByDate(today).map(Plan::getId).orElseThrow();

    toDo = ToDoFixture.customToDo("todo-1", goal.getUserId(), today, planId, goal.getId());
    fakeToDoRepository.saveToDo(toDo);
  }

  @Test
  void givenToDoExists_whenUpdateToDo_thenContentIsUpdated() {
    LocalDate today = LocalDate.now();
    UpdateToDoCommand command =
        new UpdateToDoCommand(toDo.getId(), toDo.getUserId(), "수정된 내용", today);
    // When: UseCase 실행
    updateToDoUseCase.execute(command);

    // Then: 실제로 업데이트 되었는지 확인
    ToDo updated = fakeToDoRepository.findById(toDo.getId()).orElse(null);
    assertNotNull(updated, "업데이트 후 ToDo는 null이 아니어야 한다");
    assertEquals("수정된 내용", updated.getContent(), "ToDo 내용이 정상적으로 변경되어야 한다");
  }
}
