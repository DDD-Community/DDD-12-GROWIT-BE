package com.growit.app.goal.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.domain.goal.dto.PlanDto;
import com.growit.app.goal.domain.goal.vo.BeforeAfter;
import com.growit.app.goal.domain.goal.vo.GoalDuration;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateGoalUseCaseTest {

  @Mock private GoalRepository goalRepository;

  private CreateGoalUseCase createGoalUseCase;

  @BeforeEach
  void setUp() {
    createGoalUseCase = new CreateGoalUseCase(goalRepository);
  }

  @Test
  @DisplayName("유효한 CreateGoalCommand로 목표를 생성할 수 있다")
  void shouldCreateGoalWithValidCommand() {
    // given
    CreateGoalCommand command = createValidCommand();

    // when
    String goalId = createGoalUseCase.execute(command);

    // then
    assertThat(goalId).isNotNull();
    verify(goalRepository).saveGoal(any(Goal.class));
  }

  @Test
  @DisplayName("계획이 없으면 예외가 발생한다")
  void shouldThrowExceptionWhenPlansAreEmpty() {
    // given
    CreateGoalCommand command = new CreateGoalCommand(
        "userId",
        "Test Goal",
        new GoalDuration(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)),
        new BeforeAfter("asIs", "toBe"),
        List.of()
    );

    // when & then
    assertThatThrownBy(() -> createGoalUseCase.execute(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주간 계획은 필수입니다.");
  }

  @Test
  @DisplayName("계획 내용이 비어있으면 예외가 발생한다")
  void shouldThrowExceptionWhenPlanContentIsEmpty() {
    // given
    CreateGoalCommand command = new CreateGoalCommand(
        "userId",
        "Test Goal",
        new GoalDuration(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)),
        new BeforeAfter("asIs", "toBe"),
        List.of(new PlanDto(""), new PlanDto("valid plan"))
    );

    // when & then
    assertThatThrownBy(() -> createGoalUseCase.execute(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주간 계획 내용은 필수입니다.");
  }

  @Test
  @DisplayName("계획 내용이 20자를 초과하면 예외가 발생한다")
  void shouldThrowExceptionWhenPlanContentExceeds20Characters() {
    // given
    CreateGoalCommand command = new CreateGoalCommand(
        "userId",
        "Test Goal",
        new GoalDuration(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)),
        new BeforeAfter("asIs", "toBe"),
        List.of(new PlanDto("이 계획은 20자를 초과하는 매우 긴 계획입니다"))
    );

    // when & then
    assertThatThrownBy(() -> createGoalUseCase.execute(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주간 계획은 20자 이하여야 합니다.");
  }

  private CreateGoalCommand createValidCommand() {
    return new CreateGoalCommand(
        "userId",
        "Test Goal",
        new GoalDuration(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31)),
        new BeforeAfter("현재 상태", "목표 상태"),
        List.of(
            new PlanDto("1주차 계획"),
            new PlanDto("2주차 계획"),
            new PlanDto("3주차 계획"),
            new PlanDto("4주차 계획"),
            new PlanDto("5주차 계획")
        )
    );
  }
}