package com.growit.app.todos.domain.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.todos.FakeToDoRepository;
import com.growit.app.fake.todos.ToDoFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToDoServiceTest {
  private FakeToDoRepository fakeRepo;
  private ToDoService toDoService;

  @BeforeEach
  void setUp() {
    fakeRepo = new FakeToDoRepository();
    toDoService = new ToDoService(fakeRepo);
  }

  @Test
  void givenValidDate_whenIsDateInRange_thenSuccess() {
    LocalDate today = LocalDate.now();
    toDoService.isDateInRange(today);
  }

  @Test
  void givenInvalidDate_whenIsDateInRange_thenThrowBadRequestException() {
    LocalDate past = LocalDate.now().minusWeeks(2);
    assertThrows(BadRequestException.class, () -> toDoService.isDateInRange(past));
  }

  @Test
  void givenLessThan10ToDos_whenTooManyToDoCreated_thenSuccess() {
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    for (int i = 0; i < 9; i++) {
      fakeRepo.saveToDo(ToDoFixture.customToDo("todo-" + i, userId, today));
    }
    toDoService.tooManyToDoCreated(today, userId);
  }

  @Test
  void given10OrMoreToDos_whenTooManyToDoCreated_thenThrowBadRequestException() {
    String userId = "user-1";
    LocalDate today = LocalDate.now();
    for (int i = 0; i < 10; i++) {
      fakeRepo.saveToDo(ToDoFixture.customToDo("todo-" + i, userId, today));
    }
    System.out.println("전체 저장 개수: " + fakeRepo.countByToDo(today, userId));
    assertThrows(BadRequestException.class, () -> toDoService.tooManyToDoCreated(today, userId));
  }

  @Test
  void givenValidContent_whenCheckContent_thenSuccess() {
    toDoService.checkContent("적당한 내용입니다!");
  }

  @Test
  void givenShortContent_whenCheckContent_thenThrow() {
    assertThrows(IllegalArgumentException.class, () -> toDoService.checkContent("짧다"));
  }

  @Test
  void givenLongContent_whenCheckContent_thenThrow() {
    String longContent = "이 내용은 30자를 넘어가는 매우매우매우 긴 내용입니다!";
    assertThrows(IllegalArgumentException.class, () -> toDoService.checkContent(longContent));
  }
}
