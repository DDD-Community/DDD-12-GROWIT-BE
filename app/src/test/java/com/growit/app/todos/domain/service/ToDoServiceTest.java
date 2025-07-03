package com.growit.app.todos.domain.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todos.FakeToDoRepository;
import com.growit.app.fake.todos.ToDoFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToDoServiceTest {
  private FakeGoalRepository fakeGoalRepo;
  private FakeToDoRepository fakeToDoRepo;
  private ToDoService toDoService;

  private Goal goal;

  @BeforeEach
  void setUp() {
    fakeGoalRepo = new FakeGoalRepository();
    fakeToDoRepo = new FakeToDoRepository();
    toDoService = new ToDoService(fakeToDoRepo, fakeGoalRepo);

    // Goal을 하나 만들어서 저장 (goalId 필요)
    goal = GoalFixture.defaultGoal();
    fakeGoalRepo.saveGoal(goal);
  }

  @Test
  void givenValidDate_whenIsDateInRange_thenSuccess() {
    LocalDate today = LocalDate.now();
    // isDateInRange(date, goalId)로 goalId 전달!
    toDoService.isDateInRange(today, goal.getId());
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
      fakeToDoRepo.saveToDo(ToDoFixture.customToDo("todo-" + i, userId, today, planId));
    }
    toDoService.tooManyToDoCreated(today, userId, planId);
  }

  @Test
  void given10OrMoreToDos_whenTooManyToDoCreated_thenThrowBadRequestException() {
    String userId = "user-1";
    String planId = "plan-1";
    LocalDate today = LocalDate.now();
    for (int i = 0; i < 10; i++) {
      fakeToDoRepo.saveToDo(ToDoFixture.customToDo("todo-" + i, userId, today, planId));
    }
    assertThrows(
        BadRequestException.class, () -> toDoService.tooManyToDoCreated(today, userId, planId));
  }

  @Test
  void given10ToDosButOneIsBeingUpdated_whenTooManyToDoUpdated_thenSuccess() {
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    String planId = goal.filterByDate(today).map(Plan::getId).orElseThrow();
    for (int i = 0; i < 10; i++) {
      fakeToDoRepo.saveToDo(ToDoFixture.customToDo("todo-" + i, userId, today, planId));
    }
    // 본인의 todoId 하나 빼고 9개만 남았다고 치는 식으로 test 가능
    toDoService.tooManyToDoUpdated(today, userId, planId, "todo-1");
  }

  @Test
  void given10OtherToDos_whenTooManyToDoUpdated_thenThrowBadRequestException() {
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    String planId = goal.filterByDate(today).map(Plan::getId).orElseThrow();

    for (int i = 0; i < 10; i++) {
      fakeToDoRepo.saveToDo(ToDoFixture.customToDo("todo-" + i, userId, today, planId));
    }
    assertThrows(
      BadRequestException.class,
      () -> toDoService.tooManyToDoUpdated(today, userId, planId, "todo-11"));
  }
}
