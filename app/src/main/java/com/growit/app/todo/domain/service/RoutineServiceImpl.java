package com.growit.app.todo.domain.service;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.CreateToDoCommand;
import com.growit.app.todo.domain.dto.DeleteToDoCommand;
import com.growit.app.todo.domain.dto.ToDoResult;
import com.growit.app.todo.domain.dto.UpdateToDoCommand;
import com.growit.app.todo.domain.vo.RepeatType;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.domain.vo.RoutineDuration;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {
  private final ToDoRepository toDoRepository;

  /**
   * 루틴 ToDo 일괄 생성에 필요한 값 묶음.
   *
   * <p>skipDate 는 이미 저장된 ToDo 가 있어 생성을 건너뛸 날짜다. 같은 타입의 필드가 연속돼 위치 인자로는 순서를 잘못 넣기 쉬우므로 빌더로만 만든다.
   */
  @Builder(toBuilder = true)
  private record RoutineToDoSpec(
      Routine routine,
      String userId,
      String goalId,
      String content,
      boolean isImportant,
      LocalDate baseDate,
      LocalDate startDate,
      LocalDate endDate,
      LocalDate skipDate) {}

  @Override
  public ToDoResult createRoutineToDos(CreateToDoCommand command) {
    // 루틴을 먼저 한 번만 생성 (동일한 ID로)

    RoutineToDoSpec spec =
        RoutineToDoSpec.builder()
            .routine(command.routine())
            .userId(command.userId())
            .goalId(command.goalId())
            .content(command.content())
            .isImportant(command.isImportant())
            .baseDate(command.date())
            .startDate(command.routine().getDuration().getStartDate())
            .endDate(command.routine().getDuration().getEndDate())
            .build();

    return persist(spec, resolveDates(spec), Set.of(), null);
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

    if (existingToDo.getRoutine() == null) {
      return attachRoutine(existingToDo, command);
    }

    return switch (command.routineUpdateType()) {
      case SINGLE -> updateSingleToDo(existingToDo, command);
      case FROM_DATE -> updateFromDate(existingToDo, command);
      case ALL -> updateAllRoutineToDos(existingToDo, command);
    };
  }

  /** 반복이 없던 투두에 반복을 새로 거는 경우. */
  private ToDoResult attachRoutine(ToDo existingToDo, UpdateToDoCommand command) {
    if (command.routine() == null || !command.routine().isValid()) {
      existingToDo.updateBy(command);
      toDoRepository.saveToDo(existingToDo);
      return new ToDoResult(existingToDo.getId());
    }

    RoutineDuration duration = command.routine().getDuration();
    // 회차는 반복 기간 안에서만 만든다. 대상 투두가 기간 밖이면 어떤 회차에도 속하지 않아
    // "반복이라는데 회차가 없다"는 상태가 된다.
    if (command.date().isBefore(duration.getStartDate())
        || command.date().isAfter(duration.getEndDate())) {
      throw new BadRequestException("투두 날짜가 반복 기간 밖입니다.");
    }

    existingToDo.updateBy(command);
    toDoRepository.saveToDo(existingToDo);

    // 이 투두의 날짜부터 생성하되 그 날짜는 건너뛴다. "다음 회차"를 먼저 계산해 시작일로 넘기면
    // MONTHLY 월말(1/31 -> 2월)에서 null 이 나와 회차가 하나도 만들어지지 않는다.
    RoutineToDoSpec spec =
        specFor(command, command.routine(), command.date(), duration.getEndDate()).toBuilder()
            .skipDate(command.date())
            .build();

    return persist(spec, resolveDates(spec), Set.of(), existingToDo.getId());
  }

  private ToDoResult updateSingleToDo(ToDo existingToDo, UpdateToDoCommand command) {
    // 단일 투두 수정: 해당 투두만 내용 변경하고 반복 연결은 유지
    existingToDo.updateContentOnly(command);
    toDoRepository.saveToDo(existingToDo);
    return new ToDoResult(existingToDo.getId());
  }

  private ToDoResult updateFromDate(ToDo existingToDo, UpdateToDoCommand command) {
    requireRoutine(command);

    // 기준일은 "선택한 투두의 날짜"다. 사용자가 폼에서 날짜를 바꿨더라도 어디서부터 잘라낼지는
    // 선택한 투두를 기준으로 해야 "선택한 날짜 포함, 이후 전체" 요구사항을 만족한다.
    // (삭제 경로인 deleteRoutineToDos 의 FROM_DATE 와 동일한 기준)
    LocalDate cutoffDate = existingToDo.getDate();
    List<ToDo> affected =
        toDoRepository.findByRoutineIdAndUserIdAndDateAfter(
            existingToDo.getRoutine().getId(), command.userId(), cutoffDate);

    // 일정이 그대로면 지우고 다시 만들 이유가 없다. 제자리에서 내용만 바꾸면
    // 투두 ID·완료 이력·반복 주기가 모두 보존된다.
    if (keepsSchedule(existingToDo, command)) {
      return updateInPlace(affected, existingToDo.getId(), command);
    }

    LocalDate endDate = command.routine().getDuration().getEndDate();
    if (endDate.isBefore(cutoffDate)) {
      throw new BadRequestException("반복 종료일은 수정 기준일보다 앞설 수 없습니다.");
    }

    // 뒤쪽은 새 반복으로 갈라진다. 실제 회차 범위에 맞춰 기간을 좁혀 저장한다.
    Routine splitRoutine =
        Routine.of(
            RoutineDuration.of(cutoffDate, endDate),
            command.routine().getRepeatType(),
            command.routine().getRepeatDays());

    RoutineToDoSpec spec = specFor(command, splitRoutine, cutoffDate, endDate);
    List<LocalDate> newDates = requireNonEmpty(resolveDates(spec));

    // 앞쪽에 남는 회차도 좁힌 반복으로 다시 묶는다. 그러지 않으면 앞쪽 반복이 여전히
    // 뒤쪽 날짜까지 포함한다고 기록돼, 앞쪽에서 ALL 을 누를 때 같은 날 투두가 중복된다.
    narrowPrecedingSeries(existingToDo, cutoffDate, command.userId());

    Set<LocalDate> completedDates = deleteAndCollectCompletedDates(affected);
    return persist(spec, newDates, completedDates, existingToDo.getId());
  }

  private ToDoResult updateAllRoutineToDos(ToDo existingToDo, UpdateToDoCommand command) {
    requireRoutine(command);

    List<ToDo> affected =
        toDoRepository.findByRoutineIdAndUserId(
            existingToDo.getRoutine().getId(), command.userId());

    if (keepsSchedule(existingToDo, command)) {
      return updateInPlace(affected, existingToDo.getId(), command);
    }

    RoutineDuration duration = command.routine().getDuration();
    RoutineToDoSpec spec =
        specFor(command, command.routine(), duration.getStartDate(), duration.getEndDate());
    List<LocalDate> newDates = requireNonEmpty(resolveDates(spec));

    Set<LocalDate> completedDates = deleteAndCollectCompletedDates(affected);
    return persist(spec, newDates, completedDates, existingToDo.getId());
  }

  /**
   * 일정이 그대로인지 본다. 반복 주기·요일·기간이 같고 선택한 투두의 날짜도 그대로면 회차를 다시 만들 이유가 없다. 이 경우 제자리 수정으로 처리해 ID·완료
   * 이력·주기(월말 보정, 격주 홀짝)를 보존한다.
   */
  private boolean keepsSchedule(ToDo existingToDo, UpdateToDoCommand command) {
    Routine current = existingToDo.getRoutine();
    Routine requested = command.routine();

    return command.date().equals(existingToDo.getDate())
        && current.getRepeatType() == requested.getRepeatType()
        && repeatDaysOf(current).equals(repeatDaysOf(requested))
        && current.getDuration().getStartDate().equals(requested.getDuration().getStartDate())
        && current.getDuration().getEndDate().equals(requested.getDuration().getEndDate());
  }

  private Set<DayOfWeek> repeatDaysOf(Routine routine) {
    return routine.getRepeatDays() == null ? Set.of() : new HashSet<>(routine.getRepeatDays());
  }

  private ToDoResult updateInPlace(
      List<ToDo> affected, String resultId, UpdateToDoCommand command) {
    for (ToDo toDo : affected) {
      toDo.updateContentKeepingSchedule(command);
      toDoRepository.saveToDo(toDo);
    }
    return new ToDoResult(resultId);
  }

  /**
   * 시리즈를 나눌 때 앞쪽에 남는 회차를 기준일 직전까지로 좁힌 반복에 다시 연결한다. 앞쪽 반복이 원래의 넓은 기간을 그대로 들고 있으면, 나중에 앞쪽에서 ALL 을 누를
   * 때 뒤쪽 시리즈가 이미 차지한 날짜까지 다시 만들어 같은 날 투두가 중복된다.
   */
  private void narrowPrecedingSeries(ToDo existingToDo, LocalDate cutoffDate, String userId) {
    Routine current = existingToDo.getRoutine();
    List<ToDo> preceding =
        toDoRepository.findByRoutineIdAndUserId(current.getId(), userId).stream()
            .filter(toDo -> toDo.getDate().isBefore(cutoffDate))
            .toList();

    if (preceding.isEmpty()) {
      return;
    }

    LocalDate precedingEnd = cutoffDate.minusDays(1);
    LocalDate earliest =
        preceding.stream().map(ToDo::getDate).min(LocalDate::compareTo).orElse(precedingEnd);
    LocalDate precedingStart = current.getDuration().getStartDate();
    if (precedingStart.isAfter(earliest)) {
      precedingStart = earliest;
    }

    Routine narrowed =
        Routine.of(
            RoutineDuration.of(precedingStart, precedingEnd),
            current.getRepeatType(),
            current.getRepeatDays());

    for (ToDo toDo : preceding) {
      toDo.changeRoutine(narrowed);
      toDoRepository.saveToDo(toDo);
    }
  }

  /**
   * FROM_DATE / ALL 은 대상 투두를 지우고 다시 만든다. 루틴 정보가 없으면 "무엇을 다시 만들지"를 알 수 없어 삭제만 남는다. UseCase 에서도 막지만
   * 도메인이 스스로 계약을 지키도록 같은 조건을 여기서도 거절한다.
   */
  private void requireRoutine(UpdateToDoCommand command) {
    if (command.routine() == null || !command.routine().isValid()) {
      throw new BadRequestException("반복 범위를 수정하려면 반복 설정(routine)이 필요합니다.");
    }
  }

  private RoutineToDoSpec specFor(
      UpdateToDoCommand command, Routine routine, LocalDate startDate, LocalDate endDate) {
    return RoutineToDoSpec.builder()
        .routine(routine)
        .userId(command.userId())
        .goalId(command.goalId())
        .content(command.content())
        .isImportant(command.isImportant())
        .baseDate(command.date())
        .startDate(startDate)
        .endDate(endDate)
        .build();
  }

  private List<LocalDate> resolveDates(RoutineToDoSpec spec) {
    Routine routine = spec.routine();
    return generateRoutineDatesWithDays(
            spec.baseDate(), spec.startDate(), spec.endDate(), routine.getRepeatType(), routine)
        .stream()
        // 이 날짜에는 이미 저장된 ToDo 가 있다 (기존 투두에 반복을 새로 붙이는 경우).
        .filter(date -> !date.equals(spec.skipDate()))
        .toList();
  }

  /** 회차가 하나도 안 나오는 요청은 "전부 지우고 아무것도 만들지 않는" 결과가 된다. 조용한 데이터 손실 대신 거절한다. */
  private List<LocalDate> requireNonEmpty(List<LocalDate> dates) {
    if (dates.isEmpty()) {
      throw new BadRequestException("이 반복 설정으로는 만들 수 있는 회차가 없습니다. 반복 주기와 기간을 확인해 주세요.");
    }
    return dates;
  }

  private ToDoResult persist(
      RoutineToDoSpec spec,
      List<LocalDate> dates,
      Set<LocalDate> completedDates,
      String fallbackId) {
    String firstToDoId = null;
    for (LocalDate date : dates) {
      ToDo toDo =
          ToDo.from(
              new CreateToDoCommand(
                  spec.userId(),
                  spec.goalId(),
                  spec.content(),
                  date,
                  spec.isImportant(),
                  spec.routine()));

      // 재생성 전 같은 날짜가 완료 상태였다면 사용자의 완료 이력을 그대로 이어받는다.
      if (completedDates.contains(date)) {
        toDo.updateIsCompleted(true);
      }
      toDoRepository.saveToDo(toDo);

      if (firstToDoId == null) {
        firstToDoId = toDo.getId();
      }
    }

    return new ToDoResult(firstToDoId != null ? firstToDoId : fallbackId);
  }

  /** 기준일 이후(기준일 포함) 루틴 ToDo 를 삭제한다. */
  private void deleteRoutineToDoFromDate(String routineId, LocalDate fromDate, String userId) {
    deleteAndCollectCompletedDates(
        toDoRepository.findByRoutineIdAndUserIdAndDateAfter(routineId, userId, fromDate));
  }

  /** 루틴에 속한 모든 ToDo 를 삭제한다. */
  private void deleteAllRoutineToDos(String routineId, String userId) {
    deleteAndCollectCompletedDates(toDoRepository.findByRoutineIdAndUserId(routineId, userId));
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
