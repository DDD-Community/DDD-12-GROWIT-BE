package com.growit.app.todo.usecase;

import static org.junit.jupiter.api.Assertions.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.goal.FakeGoalQuery;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.FakeToDoQuery;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.FakeToDoValidator;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.service.RoutineServiceImpl;
import com.growit.app.todo.domain.service.ToDoValidator;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdateToDoUseCaseTest {

  private UpdateToDoUseCase updateToDoUseCase;
  private FakeToDoRepository fakeToDoRepository;
  private Goal goal;
  private ToDo toDo;

  @BeforeEach
  void setUp() {
    FakeGoalRepository fakeGoalRepository = new FakeGoalRepository();
    fakeToDoRepository = new FakeToDoRepository();
    FakeToDoQuery toDoQuery = new FakeToDoQuery(fakeToDoRepository);
    ToDoValidator toDoValidator = new FakeToDoValidator();
    FakeGoalQuery goalQuery = new FakeGoalQuery(fakeGoalRepository);
    updateToDoUseCase =
        new UpdateToDoUseCase(
            toDoQuery,
            toDoValidator,
            fakeToDoRepository,
            goalQuery,
            new RoutineServiceImpl(fakeToDoRepository));

    goal = GoalFixture.defaultGoal();
    fakeGoalRepository.saveGoal(goal);

    LocalDate today = LocalDate.now();

    toDo = ToDoFixture.customToDo("todo-1", goal.getUserId(), today, goal.getId());
    fakeToDoRepository.saveToDo(toDo);
  }

  @Test
  void givenToDoExists_whenUpdateToDo_thenReturnUpdatedResultAndRepositoryUpdated() {
    // Given
    LocalDate today = LocalDate.now();
    String newContent = "수정된 내용";
    UpdateToDoCommand command =
        new UpdateToDoCommand(
            toDo.getId(), toDo.getUserId(), toDo.getGoalId(), newContent, today, false, null, null);

    // When
    ToDoResult result = updateToDoUseCase.execute(command);

    // Then: 반환값 검증
    assertNotNull(result, "반환된 ToDoResult가 null이 아니어야 한다");
    assertEquals(toDo.getId(), result.getId(), "ToDoResult의 id가 수정 대상과 같아야 한다");

    // 저장소(Repository)에서도 실제로 값이 변경됐는지 검증
    ToDo updated = fakeToDoRepository.findById(toDo.getId()).orElse(null);
    assertNotNull(updated, "업데이트 후 ToDo는 null이 아니어야 한다");
    assertEquals(newContent, updated.getContent(), "ToDo 내용이 정상적으로 변경되어야 한다");
  }

  @Test
  void givenRoutineScopeWithoutRoutine_whenUpdateToDo_thenThrowsBadRequest() {
    // Given: FROM_DATE/ALL 은 대상 투두를 지우고 다시 만드는 방식이라 루틴 정보가 없으면 대량 삭제가 된다.
    UpdateToDoCommand command =
        new UpdateToDoCommand(
            toDo.getId(),
            toDo.getUserId(),
            toDo.getGoalId(),
            "수정된 내용",
            LocalDate.now(),
            false,
            null,
            RoutineUpdateType.ALL);

    // When & Then
    assertThrows(BadRequestException.class, () -> updateToDoUseCase.execute(command));

    ToDo untouched = fakeToDoRepository.findById(toDo.getId()).orElse(null);
    assertNotNull(untouched, "요청이 거절되면 기존 ToDo는 그대로 남아야 한다");
  }
}
