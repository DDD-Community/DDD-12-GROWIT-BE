package com.growit.app.todo.usecase;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalQuery;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.service.RoutineService;
import com.growit.app.todo.domain.service.ToDoQuery;
import com.growit.app.todo.domain.service.ToDoValidator;
import com.growit.app.todo.domain.vo.RoutineUpdateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateToDoUseCase {
  private final ToDoQuery toDoQuery;
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;
  private final GoalQuery goalQuery;
  private final RoutineService routineService;

  @Transactional
  public ToDoResult execute(UpdateToDoCommand command) {
    ToDo toDo = toDoQuery.getMyToDo(command.id(), command.userId());
    if (command.goalId() != null) {
      goalQuery.getMyGoal(command.goalId(), command.userId());
    }

    if (toDo.getGoalId() != null) {
      Goal goal = goalQuery.getMyGoal(toDo.getGoalId(), command.userId());
      toDoValidator.tooManyToDoUpdated(
          command.date(), command.userId(), goal.getId(), toDo.getId());
    }

    if (command.routineUpdateType() != null) {
      validateRoutineRequired(command);
      return routineService.updateRoutineToDos(toDo, command);
    }

    toDo.updateBy(command);
    toDoRepository.saveToDo(toDo);

    return new ToDoResult(toDo.getId());
  }

  /**
   * FROM_DATE / ALL 은 대상 투두를 지우고 다시 만드는 방식이라, 루틴 정보가 없으면 "수정"이 아니라 대량 삭제가 된다. 클라이언트 실수로 데이터가 사라지지
   * 않도록 명시적으로 거절한다.
   */
  private void validateRoutineRequired(UpdateToDoCommand command) {
    boolean needsRoutine =
        command.routineUpdateType() == RoutineUpdateType.FROM_DATE
            || command.routineUpdateType() == RoutineUpdateType.ALL;

    if (needsRoutine && (command.routine() == null || !command.routine().isValid())) {
      throw new BadRequestException("반복 범위를 수정하려면 반복 설정(routine)이 필요합니다.");
    }
  }
}
