package com.growit.app.todo.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.vo.FaceStatus;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetFaceStatusUseCaseTest {

  @Mock private ToDoRepository toDoRepository;
  @InjectMocks private GetFaceStatusUseCase useCase;

  @Test
  void givenNoToDosExist_whenExecute_thenReturnSAD() {
    String userId = "user-1";
    String goalId = "goal-1";
    when(toDoRepository.findByDateFilter(any())).thenReturn(List.of());

    FaceStatus result = useCase.execute(userId, goalId);

    assertEquals(FaceStatus.SAD, result);
  }

  @Test
  void givenAllToDosAreIncomplete_whenExecute_thenReturnSAD() {
    String userId = "user-1";
    String goalId = "goal-1";
    ToDo todo = ToDoFixture.defaultToDo();
    when(toDoRepository.findByDateFilter(any())).thenReturn(List.of(todo));

    FaceStatus result = useCase.execute(userId, goalId);

    assertEquals(FaceStatus.SAD, result);
  }

  @Test
  void givenSomeToDosAreCompleted_whenExecute_thenReturnNORMAL() {
    String userId = "user-1";
    String goalId = "goal-1";
    ToDo completed = ToDoFixture.completedToDo();
    ToDo incomplete = ToDoFixture.defaultToDo();
    when(toDoRepository.findByDateFilter(any())).thenReturn(List.of(completed, incomplete));

    FaceStatus result = useCase.execute(userId, goalId);

    assertEquals(FaceStatus.NORMAL, result);
  }

  @Test
  void givenAllToDosAreCompleted_whenExecute_thenReturnHAPPY() {
    String userId = "user-1";
    String goalId = "goal-1";
    ToDo completed1 = ToDoFixture.completedToDo();
    ToDo completed2 = ToDoFixture.completedToDo();
    when(toDoRepository.findByDateFilter(any())).thenReturn(List.of(completed1, completed2));

    FaceStatus result = useCase.execute(userId, goalId);

    assertEquals(FaceStatus.HAPPY, result);
  }
}
