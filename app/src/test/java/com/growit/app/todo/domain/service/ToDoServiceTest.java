package com.growit.app.todo.domain.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.plan.vo.PlanDuration;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.vo.ToDoStatus;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToDoServiceTest {
  private FakeToDoRepository fakeToDoRepo;
  private ToDoService toDoService;

  private Goal goal;

  @BeforeEach
  void setUp() {
    FakeGoalRepository fakeGoalRepo = new FakeGoalRepository();
    fakeToDoRepo = new FakeToDoRepository();
    toDoService = new ToDoService(fakeToDoRepo);
    goal = GoalFixture.defaultGoal();
    fakeGoalRepo.saveGoal(goal);
  }

  @Test
  void givenLessThan10ToDos_whenTooManyToDoCreated_thenSuccess() {
    String userId = "user-1";
    String planId = "plan-1";
    LocalDate today = LocalDate.now();
    for (int i = 0; i < 9; i++) {
      fakeToDoRepo.saveToDo(
          ToDoFixture.customToDo("todo-" + i, userId, today, planId, goal.getId()));
    }
    toDoService.tooManyToDoCreated(today, userId, planId);
  }

  @Test
  void given10OrMoreToDos_whenTooManyToDoCreated_thenThrowBadRequestException() {
    String userId = "user-1";
    String planId = "plan-1";
    LocalDate today = LocalDate.now();
    for (int i = 0; i < 10; i++) {
      fakeToDoRepo.saveToDo(
          ToDoFixture.customToDo("todo-" + i, userId, today, planId, goal.getId()));
    }
    assertThrows(
        BadRequestException.class, () -> toDoService.tooManyToDoCreated(today, userId, planId));
  }

  @Test
  void given10ToDosButOneIsBeingUpdated_whenTooManyToDoUpdated_thenSuccess() {
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    String planId = goal.getPlanByDate(today).getId();
    for (int i = 0; i < 10; i++) {
      fakeToDoRepo.saveToDo(
          ToDoFixture.customToDo("todo-" + i, userId, today, planId, goal.getId()));
    }
    // 본인의 todoId 하나 빼고 9개만 남았다고 치는 식으로 test 가능
    toDoService.tooManyToDoUpdated(today, userId, planId, "todo-1");
  }

  @Test
  void given10OtherToDos_whenTooManyToDoUpdated_thenThrowBadRequestException() {
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    String planId = goal.getPlanByDate(today).getId();

    for (int i = 0; i < 10; i++) {
      fakeToDoRepo.saveToDo(
          ToDoFixture.customToDo("todo-" + i, userId, today, planId, goal.getId()));
    }
    assertThrows(
        BadRequestException.class,
        () -> toDoService.tooManyToDoUpdated(today, userId, planId, "todo-11"));
  }

  @Test
  void givenCorrectUserAndTodoId_whenGetMyTodo_thenReturnTodo() {
    ToDo todo = ToDoFixture.defaultToDo();
    fakeToDoRepo.saveToDo(todo);

    var result = toDoService.getMyToDo(todo.getId(), todo.getUserId());

    assertNotNull(result);
  }

  @Test
  void givenToDos_whenGetStatus_thenReturnsCorrectStatus() {
    // given
    ToDo completed =
        ToDoFixture.customToDo("todo-1", "user-1", LocalDate.now(), "plan-1", "goal-1");
    completed.updateIsCompleted(true);
    ToDo notCompleted =
        ToDoFixture.customToDo("todo-2", "user-1", LocalDate.now(), "plan-1", "goal-1");
    notCompleted.updateIsCompleted(false);

    List<ToDo> todos = List.of(completed, notCompleted);

    // when
    ToDoStatus status = ToDoStatus.getStatus(todos);

    // then
    assertThat(status).isEqualTo(ToDoStatus.IN_PROGRESS);
  }

  @Test
  void givenGoalAndToDos_whenGetContributionWithDateLimit_thenReturnsCorrectStatus() {
    // given
    LocalDate startDate = LocalDate.of(2024, 7, 1);
    LocalDate endDate = startDate.plusDays(27); // 목표 기간 마지막 날짜

    Goal goal =
        GoalFixture.customGoal(
            "goal-1",
            "user-1",
            "Goal",
            new GoalDuration(startDate, endDate),
            null,
            List.of(new Plan("plan-1", 1, "Plan", new PlanDuration(startDate, endDate))));

    ToDo completed = ToDoFixture.customToDo("todo-1", "user-1", startDate, "plan-1", "goal-1");
    completed.updateIsCompleted(true);
    ToDo notCompleted =
        ToDoFixture.customToDo("todo-2", "user-1", startDate.plusDays(1), "plan-1", "goal-1");
    notCompleted.updateIsCompleted(false);

    List<ToDo> todos = List.of(completed, notCompleted);

    System.out.println(todos.size());
    // when
    List<ToDoStatus> result = toDoService.getContribution(goal, todos);

    // then
    int expectedSize = (int) ChronoUnit.DAYS.between(startDate, LocalDate.now()) + 1;
    assertThat(result).hasSize(expectedSize);

    // 날짜별 상태 확인
    assertThat(result.get(0)).isEqualTo(ToDoStatus.COMPLETED); // 첫 번째 날짜
    assertThat(result.get(1)).isEqualTo(ToDoStatus.NOT_STARTED); // 두 번째 날짜
  }
}
