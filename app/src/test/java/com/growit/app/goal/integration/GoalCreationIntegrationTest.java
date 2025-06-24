package com.growit.app.goal.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.growit.app.goal.controller.dto.request.CreateGoalRequest;
import com.growit.app.goal.controller.mapper.GoalRequestMapper;
import com.growit.app.goal.domain.goal.dto.CreateGoalCommand;
import com.growit.app.goal.usecase.CreateGoalUseCase;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GoalCreationIntegrationTest {

  @Autowired private CreateGoalUseCase createGoalUseCase;
  @Autowired private GoalRequestMapper goalRequestMapper;

  @Test
  @DisplayName("통합 테스트: 유효한 목표 생성 요청으로 목표를 생성할 수 있다")
  void shouldCreateGoalSuccessfully() {
    // given
    CreateGoalRequest request = createValidGoalRequest();
    CreateGoalCommand command = goalRequestMapper.toCreateGoalCommand("test-user-id", request);

    // when
    String goalId = createGoalUseCase.execute(command);

    // then
    assertThat(goalId).isNotNull();
    assertThat(goalId).isNotEmpty();
  }

  @Test
  @DisplayName("통합 테스트: 잘못된 날짜로 목표 생성시 예외가 발생한다")
  void shouldFailWhenInvalidDates() {
    // given
    CreateGoalRequest request = createValidGoalRequest();
    request.setStartDate(LocalDate.of(2024, 1, 15)); // 월의 중간일

    // when & then
    assertThrows(IllegalArgumentException.class, () -> {
      goalRequestMapper.toCreateGoalCommand("test-user-id", request);
    });
  }

  private CreateGoalRequest createValidGoalRequest() {
    CreateGoalRequest request = new CreateGoalRequest();
    request.setName("영어 실력 향상");
    request.setStartDate(LocalDate.of(2024, 1, 1));
    request.setEndDate(LocalDate.of(2024, 1, 31));
    request.setAsIs("기초 수준");
    request.setToBe("중급 수준");

    List<CreateGoalRequest.PlanRequest> plans = new ArrayList<>();
    plans.add(createPlanRequest("1주차: 기초 문법"));
    plans.add(createPlanRequest("2주차: 어휘 확장"));
    plans.add(createPlanRequest("3주차: 듣기 연습"));
    plans.add(createPlanRequest("4주차: 말하기 연습"));
    request.setPlans(plans);

    return request;
  }

  private CreateGoalRequest.PlanRequest createPlanRequest(String content) {
    CreateGoalRequest.PlanRequest plan = new CreateGoalRequest.PlanRequest();
    plan.setContent(content);
    return plan;
  }
}