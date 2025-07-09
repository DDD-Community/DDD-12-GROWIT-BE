package com.growit.app.todo.domain.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import com.growit.app.todo.domain.ToDo;
import java.time.LocalDate;
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
    toDoService = new ToDoService(fakeToDoRepo, fakeGoalRepo);
    GoalDuration duration = GoalFixture.createGoalDuration(4);
    goal = GoalFixture.customGoal("goal-1", "user-1", "이름", duration, null, null);
    fakeGoalRepo.saveGoal(goal);
  }

  @Test
  void givenValidDate_whenIsDateInRange_thenSuccess() {
    toDoService.isDateInRange(goal.getDuration().startDate(), goal.getId());
  }

  @Test
  void givenInvalidDate_whenIsDateInRange_thenThrowBadRequestException() {
    LocalDate past = LocalDate.now().minusWeeks(2);
    // goalId 반드시 같이 전달!
    assertThrows(BadRequestException.class, () -> toDoService.isDateInRange(past, goal.getId()));
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
    LocalDate today = goal.getDuration().startDate();
    String planId = goal.filterByDate(today).map(Plan::getId).orElseThrow();
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
    LocalDate today = goal.getDuration().startDate();
    String planId = goal.filterByDate(today).map(Plan::getId).orElseThrow();

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
}
