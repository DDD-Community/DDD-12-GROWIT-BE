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
        command.date(),
        command.routine().getDuration().getStartDate(),
        command.routine().getDuration().getEndDate());
  }

  private List<LocalDate> generateRoutineDates(
      LocalDate baseDate, LocalDate startDate, LocalDate endDate, RepeatType repeatType) {
    List<LocalDate> dates = new ArrayList<>();
    LocalDate currentDate = findFirstDateFromBase(baseDate, startDate, endDate, repeatType);

    while (currentDate != null && !currentDate.isAfter(endDate)) {
      dates.add(currentDate);

      if (repeatType == RepeatType.MONTHLY) {
        currentDate = getNextMonthlyDateFromBase(baseDate, currentDate, endDate);
      } else {
        currentDate = getNextDate(currentDate, repeatType);
      }
    }

    return dates;
  }

  private LocalDate getNextMonthlyDateFromBase(
      LocalDate baseDate, LocalDate currentDate, LocalDate endDate) {
    int baseDayOfMonth = baseDate.getDayOfMonth();
    LocalDate nextMonth = currentDate.plusMonths(1);

    if (nextMonth.isAfter(endDate)) {
      return null;
    }

    // 다음 달에 해당 날짜가 있으면 그대로 사용
    if (baseDayOfMonth <= nextMonth.lengthOfMonth()) {
      return nextMonth.withDayOfMonth(baseDayOfMonth);
    }

    // 해당 날짜가 없으면 기준일의 월말 기준 위치로 계산
    int baseDaysFromEnd = baseDate.lengthOfMonth() - baseDayOfMonth;
    int targetDay = nextMonth.lengthOfMonth() - baseDaysFromEnd;

    return nextMonth.withDayOfMonth(targetDay);
  }

  private LocalDate findFirstDateFromBase(
      LocalDate baseDate, LocalDate startDate, LocalDate endDate, RepeatType repeatType) {
    if (repeatType == RepeatType.DAILY) {
      return startDate.isAfter(endDate) ? null : startDate;
    }

    // baseDate가 endDate보다 이후면 null 반환
    if (baseDate.isAfter(endDate)) {
      return null;
    }

    LocalDate currentDate;

    // baseDate가 startDate보다 이전이면 startDate부터 시작하여 다음 해당 요일/일자 찾기
    if (baseDate.isBefore(startDate)) {
      currentDate = startDate;
      // startDate가 baseDate와 같은 요일/일자가 아니면 다음 해당 요일/일자로 이동
      while (!isSamePattern(currentDate, baseDate, repeatType) && !currentDate.isAfter(endDate)) {
        currentDate = currentDate.plusDays(1);
      }
    }
    // baseDate가 startDate 이후면 startDate부터 시작하여 첫 번째 해당 요일/일자 찾기
    else {
      currentDate = startDate;
      // startDate에서 baseDate와 같은 요일/일자를 찾아 이동
      while (!isSamePattern(currentDate, baseDate, repeatType) && !currentDate.isAfter(endDate)) {
        currentDate = currentDate.plusDays(1);
      }
    }

    return currentDate.isAfter(endDate) ? null : currentDate;
  }

  private boolean isSamePattern(LocalDate date1, LocalDate date2, RepeatType repeatType) {
    return switch (repeatType) {
      case DAILY -> true;
      case WEEKLY, BIWEEKLY -> date1.getDayOfWeek().equals(date2.getDayOfWeek());
      case MONTHLY -> isSameMonthlyPattern(date1, date2);
    };
  }

  private boolean isSameMonthlyPattern(LocalDate date1, LocalDate date2) {
    int baseDay = date1.getDayOfMonth();
    int targetDay = date2.getDayOfMonth();

    // 기준일과 대상일이 같으면 true
    if (baseDay == targetDay) {
      return true;
    }

    // 기준일이 대상 월에 존재하지 않는 경우, 월말 기준으로 비교
    if (baseDay > date2.lengthOfMonth()) {
      int baseDaysFromEnd = date1.lengthOfMonth() - baseDay;
      int targetDaysFromEnd = date2.lengthOfMonth() - targetDay;
      return baseDaysFromEnd == targetDaysFromEnd;
    }

    return false;
  }

  private LocalDate getNextDate(LocalDate currentDate, RepeatType repeatType) {
    return switch (repeatType) {
      case DAILY -> currentDate.plusDays(1);
      case WEEKLY -> currentDate.plusWeeks(1);
      case BIWEEKLY -> currentDate.plusWeeks(2);
      case MONTHLY -> getNextMonthlyDate(currentDate);
    };
  }

  private LocalDate getNextMonthlyDate(LocalDate currentDate) {
    int dayOfMonth = currentDate.getDayOfMonth();
    LocalDate nextMonth = currentDate.plusMonths(1);

    // 다음 달에 해당 날짜가 있으면 그대로 사용
    if (dayOfMonth <= nextMonth.lengthOfMonth()) {
      return nextMonth.withDayOfMonth(dayOfMonth);
    }

    // 해당 날짜가 없으면 null 반환하여 생성하지 않음
    return null;
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
    deleteAllRoutineToDos(existingToDo.getRoutine().getId(), command.userId());

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
      CreateToDoCommand createCommand =
          new CreateToDoCommand(
              command.userId(),
              command.goalId(),
              command.content(),
              command.date(),
              command.isImportant(),
              null);
      ToDo newToDo = ToDo.from(createCommand);
      toDoRepository.saveToDo(newToDo);
      return new ToDoResult(newToDo.getId());
    }
  }

  private ToDoResult createNewRoutineFromDate(UpdateToDoCommand command, LocalDate fromDate) {
    return createToDosForRoutine(
        command.routine(),
        command.userId(),
        command.goalId(),
        command.content(),
        command.isImportant(),
        command.date(),
        fromDate,
        command.routine().getDuration().getEndDate());
  }

  private ToDoResult createToDosForRoutine(
      Routine sharedRoutine,
      String userId,
      String goalId,
      String content,
      boolean isImportant,
      LocalDate baseDate,
      LocalDate startDate,
      LocalDate endDate) {

    List<LocalDate> dates =
        generateRoutineDates(baseDate, startDate, endDate, sharedRoutine.getRepeatType());

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
