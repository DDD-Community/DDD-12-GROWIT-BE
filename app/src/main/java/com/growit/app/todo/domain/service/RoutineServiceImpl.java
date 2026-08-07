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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
  private final ToDoRepository toDoRepository;

  /** 루틴 ToDo 일괄 생성에 필요한 값 묶음. completedDates 에 해당하는 날짜는 완료 상태로 복원한다. */
  private record RoutineToDoSpec(
      Routine routine,
      String userId,
      String goalId,
      String content,
      boolean isImportant,
      LocalDate baseDate,
      LocalDate startDate,
      LocalDate endDate,
      Set<LocalDate> completedDates) {}

  @Override
  public ToDoResult createRoutineToDos(CreateToDoCommand command) {
    // 루틴을 먼저 한 번만 생성 (동일한 ID로)

    return createToDosForRoutine(
        new RoutineToDoSpec(
            command.routine(),
            command.userId(),
            command.goalId(),
            command.content(),
            command.isImportant(),
            command.date(),
            command.routine().getDuration().getStartDate(),
            command.routine().getDuration().getEndDate(),
            Set.of()));
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

  private List<LocalDate> generateRoutineDatesWithDays(
      LocalDate baseDate,
      LocalDate startDate,
      LocalDate endDate,
      RepeatType repeatType,
      Routine routine) {
    List<LocalDate> dates = new ArrayList<>();

    // 일간, 월간 반복이거나 repeatDays가 없으면 기존 로직 사용
    if (repeatType == RepeatType.DAILY
        || repeatType == RepeatType.MONTHLY
        || routine.getRepeatDays() == null
        || routine.getRepeatDays().isEmpty()) {
      return generateRoutineDates(baseDate, startDate, endDate, repeatType);
    }

    // 주간/격주 반복에서 지정된 요일만 생성
    LocalDate currentDate = startDate;

    if (repeatType == RepeatType.WEEKLY) {
      // 주간 반복: 매주 지정된 요일에 생성
      while (!currentDate.isAfter(endDate)) {
        if (routine.getRepeatDays().contains(currentDate.getDayOfWeek())) {
          dates.add(currentDate);
        }
        currentDate = currentDate.plusDays(1);
      }
    } else if (repeatType == RepeatType.BIWEEKLY) {
      // 격주 반복: 시작 주를 기준으로 격주마다 지정된 요일에 생성
      LocalDate weekStart = startDate.minusDays(startDate.getDayOfWeek().getValue() - 1);
      int weekCount = 0;

      while (!currentDate.isAfter(endDate)) {
        LocalDate currentWeekStart = weekStart.plusWeeks(weekCount);

        // 격주 주인지 확인 (짝수 번째 주)
        if (weekCount % 2 == 0) {
          for (int dayOffset = 0; dayOffset < 7; dayOffset++) {
            LocalDate checkDate = currentWeekStart.plusDays(dayOffset);
            if (!checkDate.isBefore(startDate)
                && !checkDate.isAfter(endDate)
                && routine.getRepeatDays().contains(checkDate.getDayOfWeek())) {
              dates.add(checkDate);
            }
          }
        }

        weekCount++;
        currentDate = currentWeekStart.plusWeeks(1);
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
      // MONTHLY 는 다음 달에 같은 일자가 없으면 null 을 반환한다 (예: 1/31 -> 2월).
      // 이 경우 반복 생성을 건너뛰고 단일 수정만 반영한다.
      if (nextDate == null) {
        return new ToDoResult(existingToDo.getId());
      }
      return createNewRoutineFromDate(command, nextDate, Set.of());
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
    // 단일 투두 수정: 해당 투두만 내용 변경하고 반복 연결은 유지
    existingToDo.updateContentOnly(command);
    toDoRepository.saveToDo(existingToDo);
    return new ToDoResult(existingToDo.getId());
  }

  private ToDoResult updateFromDate(ToDo existingToDo, UpdateToDoCommand command) {
    // 루틴 정보 없이 FROM_DATE 를 요청하면 "삭제만 하고 재생성하지 않는" 파괴적 동작이 된다.
    // UseCase 에서 400 으로 막지만, 도메인 차원에서도 단일 수정으로 축소해 데이터 손실을 막는다.
    if (command.routine() == null || !command.routine().isValid()) {
      return updateSingleToDo(existingToDo, command);
    }

    // 기준일은 "선택한 투두의 날짜"다. 사용자가 폼에서 날짜를 바꿨더라도 어디서부터 잘라낼지는
    // 선택한 투두를 기준으로 해야 "선택한 날짜 포함, 이후 전체" 요구사항을 만족한다.
    // (삭제 경로인 deleteRoutineToDos 의 FROM_DATE 와 동일한 기준)
    LocalDate cutoffDate = existingToDo.getDate();
    Set<LocalDate> completedDates =
        deleteRoutineToDoFromDate(existingToDo.getRoutine().getId(), cutoffDate, command.userId());

    return createNewRoutineFromDate(command, cutoffDate, completedDates);
  }

  private ToDoResult updateAllRoutineToDos(ToDo existingToDo, UpdateToDoCommand command) {
    // updateFromDate 와 동일한 이유로, 루틴 정보가 없으면 전체 삭제 대신 단일 수정으로 축소한다.
    if (command.routine() == null || !command.routine().isValid()) {
      return updateSingleToDo(existingToDo, command);
    }

    Set<LocalDate> completedDates =
        deleteAllRoutineToDos(existingToDo.getRoutine().getId(), command.userId());

    return createToDosForRoutine(
        new RoutineToDoSpec(
            command.routine(),
            command.userId(),
            command.goalId(),
            command.content(),
            command.isImportant(),
            command.date(),
            command.routine().getDuration().getStartDate(),
            command.routine().getDuration().getEndDate(),
            completedDates));
  }

  private ToDoResult createNewRoutineFromDate(
      UpdateToDoCommand command, LocalDate fromDate, Set<LocalDate> completedDates) {
    return createToDosForRoutine(
        new RoutineToDoSpec(
            command.routine(),
            command.userId(),
            command.goalId(),
            command.content(),
            command.isImportant(),
            command.date(),
            fromDate,
            command.routine().getDuration().getEndDate(),
            completedDates));
  }

  private ToDoResult createToDosForRoutine(RoutineToDoSpec spec) {
    Routine sharedRoutine = spec.routine();
    List<LocalDate> dates =
        generateRoutineDatesWithDays(
            spec.baseDate(),
            spec.startDate(),
            spec.endDate(),
            sharedRoutine.getRepeatType(),
            sharedRoutine);

    String firstToDoId = null;
    for (LocalDate date : dates) {
      CreateToDoCommand routineCommand =
          new CreateToDoCommand(
              spec.userId(),
              spec.goalId(),
              spec.content(),
              date,
              spec.isImportant(),
              sharedRoutine);

      ToDo toDo = ToDo.from(routineCommand);
      // 재생성 전 같은 날짜가 완료 상태였다면 사용자의 완료 이력을 그대로 이어받는다.
      if (spec.completedDates().contains(date)) {
        toDo.updateIsCompleted(true);
      }
      toDoRepository.saveToDo(toDo);

      if (firstToDoId == null) {
        firstToDoId = toDo.getId();
      }
    }

    return new ToDoResult(firstToDoId);
  }

  /** 기준일 이후(기준일 포함) 루틴 ToDo 를 삭제하고, 삭제된 것 중 완료 상태였던 날짜를 돌려준다. */
  private Set<LocalDate> deleteRoutineToDoFromDate(
      String routineId, LocalDate fromDate, String userId) {
    List<ToDo> routineToDos =
        toDoRepository.findByRoutineIdAndUserIdAndDateAfter(routineId, userId, fromDate);
    return deleteAndCollectCompletedDates(routineToDos);
  }

  /** 루틴에 속한 모든 ToDo 를 삭제하고, 삭제된 것 중 완료 상태였던 날짜를 돌려준다. */
  private Set<LocalDate> deleteAllRoutineToDos(String routineId, String userId) {
    List<ToDo> routineToDos = toDoRepository.findByRoutineIdAndUserId(routineId, userId);
    return deleteAndCollectCompletedDates(routineToDos);
  }

  private Set<LocalDate> deleteAndCollectCompletedDates(List<ToDo> routineToDos) {
    Set<LocalDate> completedDates = new HashSet<>();
    for (ToDo toDo : routineToDos) {
      if (toDo.isCompleted()) {
        completedDates.add(toDo.getDate());
      }
      toDoRepository.deleteToDo(toDo.getId());
    }
    return completedDates;
  }

  @Override
  public void deleteRoutineToDos(ToDo existingToDo, DeleteToDoCommand command) {
    if (command.routineDeleteType() == null || existingToDo.getRoutine() == null) {
      toDoRepository.deleteToDo(existingToDo.getId());
      return;
    }

    switch (command.routineDeleteType()) {
      case SINGLE -> toDoRepository.deleteToDo(existingToDo.getId());
      // 선택한 투두의 날짜를 포함해서 이후 전부 삭제한다.
      case FROM_DATE ->
          deleteRoutineToDoFromDate(
              existingToDo.getRoutine().getId(), existingToDo.getDate(), command.userId());
      case ALL -> deleteAllRoutineToDos(existingToDo.getRoutine().getId(), command.userId());
    }
  }
}
