package com.growit.app.goal.domain.goalrecommendation.service;

import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationRequest;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationResponse;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goalrecommendation.vo.GoalRecommendationData;
import com.growit.app.user.domain.user.User;
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
   */
  public void generateRecommendation(User user, Goal goal, GoalRecommendationData data) {
    AiGoalRecommendationRequest request = createAiRequest(user, goal, data);
    AiGoalRecommendationResponse response = aiMentorAdviceClient.getGoalRecommendation(request);
    
    // Process recommendation result (logging, analytics, etc.)
    processRecommendation(user, goal, response.getOutput());
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
    // Mentor is no longer part of Goal domain, use default
    return "teamcook-goal-001"; // Default prompt ID
  }

  private void processRecommendation(User user, Goal goal, String content) {
    // Process the recommendation result
    // This could include logging, analytics, notifications, etc.
    String recommendationId = IDGenerator.generateId();
    // Log or process the recommendation as needed
  }
}
