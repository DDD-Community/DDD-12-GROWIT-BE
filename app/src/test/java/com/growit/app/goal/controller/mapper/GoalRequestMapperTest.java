package com.growit.app.goal.controller.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GoalRequestMapperTest {

  private GoalRequestMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new GoalRequestMapper();
  }

  @Test
  @DisplayName("유효한 요청을 CreateGoalCommand로 변환할 수 있다")
  void shouldMapValidRequestToCommand() {
    // given
    CreateGoalRequest request = createValidRequest();

    // when
    CreateGoalCommand command = mapper.toCreateGoalCommand("userId", request);

    // then
    assertThat(command.userId()).isEqualTo("userId");
    assertThat(command.name()).isEqualTo("Test Goal");
    assertThat(command.duration().startDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    assertThat(command.duration().endDate()).isEqualTo(LocalDate.of(2024, 1, 31));
    assertThat(command.beforeAfter().asIs()).isEqualTo("현재 상태");
    assertThat(command.beforeAfter().toBe()).isEqualTo("목표 상태");
    assertThat(command.plans()).hasSize(4); // 4 weeks, not 5
  }

  @Test
  @DisplayName("시작일이 월의 첫날이 아니면 예외가 발생한다")
  void shouldThrowExceptionWhenStartDateIsNotFirstDayOfMonth() {
    // given
    CreateGoalRequest request = createValidRequest();
    request.setStartDate(LocalDate.of(2024, 1, 15)); // 15일

    // when & then
    assertThatThrownBy(() -> mapper.toCreateGoalCommand("userId", request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("목표 시작일은 월의 첫 날이어야 합니다.");
  }

  @Test
  @DisplayName("종료일이 월의 마지막날이 아니면 예외가 발생한다")
  void shouldThrowExceptionWhenEndDateIsNotLastDayOfMonth() {
    // given
    CreateGoalRequest request = createValidRequest();
    request.setEndDate(LocalDate.of(2024, 1, 15)); // 15일 (31일이 마지막)

    // when & then
    assertThatThrownBy(() -> mapper.toCreateGoalCommand("userId", request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("목표 종료일은 월의 마지막 날이어야 합니다.");
  }

  @Test
  @DisplayName("계획 수가 주 수와 맞지 않으면 예외가 발생한다")
  void shouldThrowExceptionWhenPlanCountDoesNotMatchWeeks() {
    // given
    CreateGoalRequest request = createValidRequest();
    // 1월은 4주이지만 계획을 3개만 제공
    request.getPlans().clear();
    request.getPlans().addAll(List.of(
        createPlanRequest("1주차"),
        createPlanRequest("2주차"),
        createPlanRequest("3주차")
    ));

    // when & then
    assertThatThrownBy(() -> mapper.toCreateGoalCommand("userId", request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("목표 기간(4주)에 맞는 주간 계획이 필요합니다. 현재: 3개");
  }

  private CreateGoalRequest createValidRequest() {
    CreateGoalRequest request = new CreateGoalRequest();
    request.setName("Test Goal");
    request.setStartDate(LocalDate.of(2024, 1, 1));
    request.setEndDate(LocalDate.of(2024, 1, 31));
    request.setAsIs("현재 상태");
    request.setToBe("목표 상태");
    
    // Create mutable list with 4 plans for 4 weeks
    java.util.ArrayList<CreateGoalRequest.PlanRequest> plans = new java.util.ArrayList<>();
    plans.add(createPlanRequest("1주차 계획"));
    plans.add(createPlanRequest("2주차 계획"));
    plans.add(createPlanRequest("3주차 계획"));
    plans.add(createPlanRequest("4주차 계획"));
    request.setPlans(plans);
    
    return request;
  }

  private CreateGoalRequest.PlanRequest createPlanRequest(String content) {
    CreateGoalRequest.PlanRequest planRequest = new CreateGoalRequest.PlanRequest();
    planRequest.setContent(content);
    return planRequest;
  }
}