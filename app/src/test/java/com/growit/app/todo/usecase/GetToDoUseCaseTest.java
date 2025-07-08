package com.growit.app.todo.usecase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.fake.todo.FakeToDoQuery;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.dto.GetToDoQueryFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetToDoUseCaseTest {

  private GetToDoUseCase getToDoUseCase;
  private ToDo toDo;

  @BeforeEach
  void setUp() {
    FakeToDoRepository fakeToDoRepository = new FakeToDoRepository();
    toDo = ToDoFixture.defaultToDo();
    fakeToDoRepository.saveToDo(toDo);

    getToDoUseCase = new GetToDoUseCase(new FakeToDoQuery(fakeToDoRepository));
  }

  @Test
  void givenValidFilter_whenExecute_thenSuccess() {
    GetToDoQueryFilter query = new GetToDoQueryFilter(toDo.getId(), toDo.getUserId());

    ToDo result = getToDoUseCase.execute(query);

    assertThat(result.getId()).isEqualTo(toDo.getId());
  }

  @Test
  void givenNonExistentId_whenExecute_thenThrowsNotFoundException() {
    String nonExistentId = "non-existent-id";
    GetToDoQueryFilter query = new GetToDoQueryFilter(nonExistentId, toDo.getUserId());

    assertThrows(NotFoundException.class, () -> getToDoUseCase.execute(query));
  }

  @Test
  void givenNotOwnerUserId_whenExecute_thenThrowsNotFoundException() {
    String notOwnerUserId = "not-owner-user";

    GetToDoQueryFilter query = new GetToDoQueryFilter(toDo.getId(), notOwnerUserId);

    assertThrows(NotFoundException.class, () -> getToDoUseCase.execute(query));
  }
}
