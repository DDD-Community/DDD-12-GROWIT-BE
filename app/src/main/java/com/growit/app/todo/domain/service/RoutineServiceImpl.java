package com.growit.app.todo.domain.service;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.DeleteToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.vo.RepeatType;
import com.growit.app.todo.domain.vo.Routine;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
  private final ToDoRepository toDoRepository;

  @Override
  public ToDoResult createRoutineToDos(CreateToDoCommand command) {
    // 루틴을 먼저 한 번만 생성 (동일한 ID로)

    return createToDosForRoutine(
        command.routine(),
        command.userId(),
        command.goalId(),
        command.content(),
        command.isImportant(),
        command.routine().getDuration().getStartDate(),
        command.routine().getDuration().getEndDate());
  }

  private List<LocalDate> generateRoutineDates(
      LocalDate startDate, LocalDate endDate, RepeatType repeatType) {
    List<LocalDate> dates = new ArrayList<>();
    LocalDate currentDate = startDate;

    while (!currentDate.isAfter(endDate)) {
      dates.add(currentDate);
      currentDate = getNextDate(currentDate, repeatType);
    }

    return dates;
  }

  private LocalDate getNextDate(LocalDate currentDate, RepeatType repeatType) {
    return switch (repeatType) {
      case DAILY -> currentDate.plusDays(1);
      case WEEKLY -> currentDate.plusWeeks(1);
      case BIWEEKLY -> currentDate.plusWeeks(2);
      case MONTHLY -> currentDate.plusMonths(1);
    };
  }

  @Override
  public ToDoResult updateRoutineToDos(ToDo existingToDo, UpdateToDoCommand command) {
    if (command.routineUpdateType() == null) {
      return new ToDoResult(existingToDo.getId());
    }

    // 기존 루틴이 없고 새로운 루틴을 추가하는 경우
    if (existingToDo.getRoutine() == null
        && command.routine() != null
        && command.routine().isValid()) {
      existingToDo.updateBy(command);
      toDoRepository.saveToDo(existingToDo);
      // 현재 날짜 다음날부터 루틴 생성
      LocalDate nextDate = getNextDate(command.date(), command.routine().getRepeatType());
      return createNewRoutineFromDate(command, nextDate);
    }

    // 기존 루틴이 없으면 단순 업데이트
    if (existingToDo.getRoutine() == null) {
      existingToDo.updateBy(command);
      toDoRepository.saveToDo(existingToDo);
      return new ToDoResult(existingToDo.getId());
    }

    return switch (command.routineUpdateType()) {
      case SINGLE -> updateSingleToDo(existingToDo, command);
      case FROM_DATE -> updateFromDate(existingToDo, command);
      case ALL -> updateAllRoutineToDos(existingToDo, command);
    };
  }

  private ToDoResult updateSingleToDo(ToDo existingToDo, UpdateToDoCommand command) {
    if (command.routine() != null && !command.routine().equals(existingToDo.getRoutine())) {
      createNewRoutineFromDate(command, command.date());
      removeRoutineFromToDo();
    }
    return new ToDoResult(existingToDo.getId());
  }

  private ToDoResult updateFromDate(ToDo existingToDo, UpdateToDoCommand command) {
    deleteRoutineToDoFromDate(existingToDo.getRoutine().getId(), command.date(), command.userId());

    if (command.routine() != null && command.routine().isValid()) {
      return createNewRoutineFromDate(command, command.date());
    }

    return new ToDoResult(existingToDo.getId());
  }

  private ToDoResult updateAllRoutineToDos(ToDo existingToDo, UpdateToDoCommand command) {
    deleteOtherRoutineToDos(
        existingToDo.getRoutine().getId(), existingToDo.getId(), command.userId());

    if (command.routine() != null && command.routine().isValid()) {
      CreateToDoCommand createCommand =
          new CreateToDoCommand(
              command.userId(),
              command.goalId(),
              command.content(),
              command.date(),
              command.isImportant(),
              command.routine());
      return createRoutineToDos(createCommand);
    } else {
      existingToDo.updateBy(command);
      existingToDo.removeRoutine();
      toDoRepository.saveToDo(existingToDo);
      return new ToDoResult(existingToDo.getId());
    }
  }

  private ToDoResult createNewRoutineFromDate(UpdateToDoCommand command, LocalDate fromDate) {
    return createToDosForRoutine(
        command.routine(),
        command.userId(),
        command.goalId(),
        command.content(),
        command.isImportant(),
        fromDate,
        command.routine().getDuration().getEndDate());
  }

  private ToDoResult createToDosForRoutine(
      Routine sharedRoutine,
      String userId,
      String goalId,
      String content,
      boolean isImportant,
      LocalDate startDate,
      LocalDate endDate) {

    List<LocalDate> dates = generateRoutineDates(startDate, endDate, sharedRoutine.getRepeatType());

    String firstToDoId = null;
    for (LocalDate date : dates) {
      CreateToDoCommand routineCommand =
          new CreateToDoCommand(userId, goalId, content, date, isImportant, sharedRoutine);

      ToDo toDo = ToDo.from(routineCommand);
      toDoRepository.saveToDo(toDo);

      if (firstToDoId == null) {
        firstToDoId = toDo.getId();
      }
    }

    return new ToDoResult(firstToDoId);
  }

  private void deleteRoutineToDoFromDate(String routineId, LocalDate fromDate, String userId) {
    List<ToDo> routineToDos =
        toDoRepository.findByRoutineIdAndUserIdAndDateAfter(routineId, userId, fromDate);
    for (ToDo toDo : routineToDos) {
      toDoRepository.deleteToDo(toDo.getId());
    }
  }

  private void deleteAllRoutineToDos(String routineId, String userId) {
    List<ToDo> routineToDos = toDoRepository.findByRoutineIdAndUserId(routineId, userId);
    for (ToDo toDo : routineToDos) {
      toDoRepository.deleteToDo(toDo.getId());
    }
  }

  private void deleteOtherRoutineToDos(String routineId, String excludeToDoId, String userId) {
    List<ToDo> routineToDos = toDoRepository.findByRoutineIdAndUserId(routineId, userId);
    for (ToDo toDo : routineToDos) {
      if (!toDo.getId().equals(excludeToDoId)) {
        toDoRepository.deleteToDo(toDo.getId());
      }
    }
  }

  private void removeRoutineFromToDo() {
    // 루틴 정보 제거 로직 (도메인 메서드 필요)
  }

  @Override
  public void deleteRoutineToDos(ToDo existingToDo, DeleteToDoCommand command) {
    if (command.routineDeleteType() == null || existingToDo.getRoutine() == null) {
      toDoRepository.deleteToDo(existingToDo.getId());
      return;
    }

    switch (command.routineDeleteType()) {
      case SINGLE -> toDoRepository.deleteToDo(existingToDo.getId());
      case FROM_DATE ->
          deleteRoutineToDoFromDate(
              existingToDo.getRoutine().getId(), existingToDo.getDate(), command.userId());
      case ALL -> deleteAllRoutineToDos(existingToDo.getRoutine().getId(), command.userId());
    }
  }
}
