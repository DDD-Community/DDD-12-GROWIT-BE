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

    validateScopeSpecified(toDo, command);

    if (toDo.getRoutine() != null) {
      // 반복 투두에 범위를 지정하지 않은 수정은 "해당 투두만"으로 본다.
      // updateBy 를 쓰면 routine 이 null 로 덮여 이 투두만 반복에서 영구히 떨어져 나간다.
      toDo.updateContentOnly(command);
    } else {
      toDo.updateBy(command);
    }
    toDoRepository.saveToDo(toDo);

    return new ToDoResult(toDo.getId());
  }

  /**
   * routine 을 보내면서 범위를 지정하지 않은 요청은 의도를 알 수 없다. ALL 로 추측하면 완료 이력이 날아가고, SINGLE 로 추측하면 사용자가 바꾼 반복 설정이
   * 조용히 버려진다. 어느 쪽도 맞지 않으므로 클라이언트에게 명시를 요구한다.
   */
  private void validateScopeSpecified(ToDo toDo, UpdateToDoCommand command) {
    if (command.routine() != null && toDo.getRoutine() != null) {
      throw new BadRequestException("반복 설정을 변경하려면 적용 범위(routineUpdateType)를 지정해야 합니다.");
    }
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
