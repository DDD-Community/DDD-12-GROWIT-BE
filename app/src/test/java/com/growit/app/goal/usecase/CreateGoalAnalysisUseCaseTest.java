package com.growit.app.goal.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.fake.goal.FakeAnalysisRepository;
import com.growit.app.fake.goal.FakeGoalQuery;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.todo.FakeToDoQuery;
import com.growit.app.fake.todo.FakeToDoRepository;
import com.growit.app.fake.todo.ToDoFixture;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.retrospect.domain.goalretrospect.service.AIAnalysis;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.infrastructure.engine.dto.AnalysisDto;
import com.growit.app.todo.domain.ToDo;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateGoalAnalysisUseCaseTest {

  @Mock private AIAnalysis aiAnalysis;

  private FakeGoalRepository goalRepository;
  private FakeGoalQuery goalQuery;
  private FakeToDoRepository toDoRepository;
  private FakeToDoQuery toDoQuery;
  private FakeAnalysisRepository analysisRepository;
  private CreateGoalAnalysisUseCase createGoalAnalysisUseCase;

  @BeforeEach
  void setUp() {
    goalRepository = new FakeGoalRepository();
    goalQuery = new FakeGoalQuery(goalRepository);
    toDoRepository = new FakeToDoRepository();
    toDoQuery = new FakeToDoQuery(toDoRepository);
    analysisRepository = new FakeAnalysisRepository();

    createGoalAnalysisUseCase =
        new CreateGoalAnalysisUseCase(goalQuery, toDoQuery, analysisRepository, aiAnalysis);
  }

  @Test
  void execute_success() {
    // given
    Goal completedGoal = GoalFixture.defaultGoal();
    completedGoal.complete(); // 목표 완료 상태로 설정
    goalRepository.saveGoal(completedGoal);

    ToDo completedToDo =
        ToDoFixture.customToDo("todo-1", "user-1", LocalDate.now(), completedGoal.getId());
    completedToDo.updateIsCompleted(true);
    ToDo incompleteToDo =
        ToDoFixture.customToDo("todo-2", "user-1", LocalDate.now(), completedGoal.getId());
    toDoRepository.saveToDo(completedToDo);
    toDoRepository.saveToDo(incompleteToDo);

    Analysis mockAnalysis = new Analysis("AI 분석 결과: 목표가 성공적으로 달성되었습니다.", "계속해서 좋은 성과를 내세요!");
    when(aiAnalysis.generate(any(AnalysisDto.class))).thenReturn(mockAnalysis);

    String goalId = completedGoal.getId();
    String userId = "user-1";

    // when
    createGoalAnalysisUseCase.execute(goalId, userId);

    // then
    verify(aiAnalysis).generate(any(AnalysisDto.class));

    GoalAnalysis savedAnalysis = analysisRepository.findByGoalId(goalId).orElseThrow();
    assertEquals(50, savedAnalysis.todoCompletedRate()); // 2개 중 1개 완료 = 50%
    assertEquals("AI 분석 결과: 목표가 성공적으로 달성되었습니다.", savedAnalysis.summary());
  }

  @Test
  void execute_goalNotCompleted_throwsBadRequestException() {
    // given
    Goal incompleteGoal = GoalFixture.defaultGoal(); // 완료되지 않은 목표
    goalRepository.saveGoal(incompleteGoal);

    String goalId = incompleteGoal.getId();
    String userId = "user-1";

    // when & then
    BadRequestException exception =
        assertThrows(
            BadRequestException.class, () -> createGoalAnalysisUseCase.execute(goalId, userId));

    assertEquals("목표가 완료되지 않았습니다", exception.getMessage());
    verifyNoInteractions(aiAnalysis);
  }

  @Test
  void execute_noToDos_success() {
    // given
    Goal completedGoal = GoalFixture.defaultGoal();
    completedGoal.complete();
    goalRepository.saveGoal(completedGoal);

    Analysis mockAnalysis = new Analysis("AI 분석 결과: ToDo가 없는 목표입니다.", "다음에는 더 구체적인 계획을 세워보세요.");
    when(aiAnalysis.generate(any(AnalysisDto.class))).thenReturn(mockAnalysis);

    String goalId = completedGoal.getId();
    String userId = "user-1";

    // when
    createGoalAnalysisUseCase.execute(goalId, userId);

    // then

    GoalAnalysis savedAnalysis = analysisRepository.findByGoalId(goalId).orElseThrow();
    assertEquals(0, savedAnalysis.todoCompletedRate());
    assertEquals("AI 분석 결과: ToDo가 없는 목표입니다.", savedAnalysis.summary());
  }

  @Test
  void execute_allToDosCompleted_success() {
    // given
    Goal completedGoal = GoalFixture.defaultGoal();
    completedGoal.complete();
    goalRepository.saveGoal(completedGoal);

    ToDo completedToDo1 =
        ToDoFixture.customToDo("todo-1", "user-1", LocalDate.now(), completedGoal.getId());
    completedToDo1.updateIsCompleted(true);
    ToDo completedToDo2 =
        ToDoFixture.customToDo("todo-2", "user-1", LocalDate.now(), completedGoal.getId());
    completedToDo2.updateIsCompleted(true);
    toDoRepository.saveToDo(completedToDo1);
    toDoRepository.saveToDo(completedToDo2);

    Analysis mockAnalysis = new Analysis("AI 분석 결과: 모든 ToDo가 완료되었습니다.", "훌륭한 성과입니다!");
    when(aiAnalysis.generate(any(AnalysisDto.class))).thenReturn(mockAnalysis);

    String goalId = completedGoal.getId();
    String userId = "user-1";

    // when
    createGoalAnalysisUseCase.execute(goalId, userId);

    // then

    GoalAnalysis savedAnalysis = analysisRepository.findByGoalId(goalId).orElseThrow();
    assertEquals(100, savedAnalysis.todoCompletedRate());
    assertEquals("AI 분석 결과: 모든 ToDo가 완료되었습니다.", savedAnalysis.summary());
  }
}
