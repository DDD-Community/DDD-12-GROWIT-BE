package com.growit.app.goal.domain.goalrecommendation.service;

import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationRequest;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationResponse;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.plan.Plan;
import com.growit.app.goal.domain.goalrecommendation.vo.GoalRecommendationData;
import com.growit.app.goal.domain.planrecommendation.PlanRecommendation;
import com.growit.app.user.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** 목표 추천 생성을 담당하는 도메인 서비스 - AI 요청 생성 - AI 호출 - 추천 객체 생성 */
@Service
@RequiredArgsConstructor
public class GoalRecommendationService {

  private final AiMentorAdviceClient aiMentorAdviceClient;

  /**
   * 목표 추천을 생성합니다.
   *
   * @param user 사용자
   * @param goal 현재 목표
   * @param data 추천 생성에 필요한 데이터
   * @return 생성된 계획 추천
   */
  public PlanRecommendation generateRecommendation(
      User user, Goal goal, GoalRecommendationData data) {
    AiGoalRecommendationRequest request = createAiRequest(user, goal, data);
    AiGoalRecommendationResponse response = aiMentorAdviceClient.getGoalRecommendation(request);

    Plan targetPlan = selectTargetPlan(goal);

    return createPlanRecommendation(user, goal, targetPlan, response.getOutput());
  }

  private AiGoalRecommendationRequest createAiRequest(
      User user, Goal goal, GoalRecommendationData data) {
    AiGoalRecommendationRequest.Input input =
        AiGoalRecommendationRequest.Input.builder()
            .pastTodos(data.getPastTodos())
            .pastRetrospects(data.getPastRetrospects())
            .overallGoal(goal.getName())
            .completedTodos(data.getCompletedTodos())
            .pastWeeklyGoals(data.getPastWeeklyGoals())
            .remainingTime(data.getRemainingTime())
            .build();

    // Goal에 저장된 mentor 정보로 promptId 결정
    String promptId = determinePromptIdByMentor(goal);

    return AiGoalRecommendationRequest.builder()
        .userId(user.getId())
        .promptId(promptId)
        .input(input)
        .build();
  }

  /** Goal의 mentor 정보를 바탕으로 적절한 promptId를 결정합니다. */
  private String determinePromptIdByMentor(Goal goal) {
    if (goal.getMentor() == null) {
      throw new BadRequestException("목표에 멘토 정보가 설정되지 않았습니다. 목표 추천을 생성할 수 없습니다.");
    }

    // Goal 엔티티의 mentor 값에 따라 분기
    return switch (goal.getMentor()) {
      case TIM_COOK -> "teamcook-goal-001";
      case WARREN_BUFFETT -> "warren-buffett-goal-001";
      case CONFUCIUS -> "confucius-goal-001";
      default -> {
        // 알 수 없는 멘토인 경우 기본값으로 팀 쿡 사용
        throw new BadRequestException("알 수 없는 멘토입니다: " + goal.getMentor());
      }
    };
  }

  private Plan selectTargetPlan(Goal goal) {
    List<Plan> plans = goal.getPlans();

    // 플랜이 없는 경우 예외 발생
    if (plans == null || plans.isEmpty()) {
      throw new BadRequestException("목표에 주간 플랜이 설정되지 않았습니다. 목표 추천을 생성할 수 없습니다.");
    }

    return plans.stream()
        .filter(Plan::isCurrentWeek)
        .findFirst()
        .orElse(plans.get(0)); // 이제 안전함 (위에서 empty 체크 완료)
  }

  private PlanRecommendation createPlanRecommendation(
      User user, Goal goal, Plan targetPlan, String content) {
    return new PlanRecommendation(
        IDGenerator.generateId(), user.getId(), goal.getId(), targetPlan.getId(), content);
  }
}
