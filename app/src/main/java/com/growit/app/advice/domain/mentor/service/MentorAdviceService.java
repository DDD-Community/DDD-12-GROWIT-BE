package com.growit.app.advice.domain.mentor.service;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.vo.MentorAdviceData;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;
import com.growit.app.common.util.IDGenerator;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** 멘토 조언 생성을 담당하는 도메인 서비스 - AI 요청 생성 - AI 호출 - 멘토 조언 객체 생성 */
@Service
@RequiredArgsConstructor
public class MentorAdviceService {

  private final AiMentorAdviceClient aiMentorAdviceClient;

  /**
   * 멘토 조언을 생성합니다.
   *
   * @param user 사용자
   * @param goal 현재 목표
   * @param data 조언 생성에 필요한 데이터
   * @return 생성된 멘토 조언
   */
  public MentorAdvice generateAdvice(User user, Goal goal, MentorAdviceData data) {
    AiMentorAdviceRequest request = createAiRequest(user, goal, data);
    AiMentorAdviceResponse response = aiMentorAdviceClient.getMentorAdvice(request);

    return createMentorAdvice(user, goal, response);
  }

  private AiMentorAdviceRequest createAiRequest(User user, Goal goal, MentorAdviceData data) {
    AiMentorAdviceRequest.Input input =
        AiMentorAdviceRequest.Input.builder()
            .recentTodos(data.getRecentTodos())
            .weeklyRetrospects(data.getWeeklyRetrospects())
            .overallGoal(data.getOverallGoal())
            .completedTodos(data.getCompletedTodos())
            .incompleteTodos(data.getIncompleteTodos())
            .pastWeeklyGoals(data.getPastWeeklyGoals())
            .build();

    // Goal에 저장된 mentor 정보로 promptId 결정
    String promptId = determineAdvicePromptIdByMentor(goal);

    return AiMentorAdviceRequest.builder()
        .userId(user.getId())
        .promptId(promptId)
        .input(input)
        .build();
  }

  /** Goal의 mentor 정보를 바탕으로 적절한 조언 promptId를 결정합니다. */
  private String determineAdvicePromptIdByMentor(Goal goal) {
    if (goal.getMentor() == null) {
      throw new IllegalArgumentException("목표에 멘토 정보가 설정되지 않았습니다.");
    }

    // Goal 엔티티의 mentor 값에 따라 분기
    return switch (goal.getMentor()) {
      case TIM_COOK -> "teamcook-advice-001";
      case WARREN_BUFFETT -> "warren-buffett-advice-001";
      case CONFUCIUS -> "confucius-advice-001";
      default -> {
        throw new IllegalArgumentException("알 수 없는 멘토입니다: " + goal.getMentor());
      }
    };
  }

  private MentorAdvice createMentorAdvice(User user, Goal goal, AiMentorAdviceResponse response) {
    return MentorAdvice.builder()
        .id(IDGenerator.generateId())
        .userId(user.getId())
        .goalId(goal.getId())
        .isChecked(false)
        .message(response.getOutput().getCopywriting())
        .kpt(
            new MentorAdvice.Kpt(
                response.getOutput().getKeep(),
                response.getOutput().getProblem(),
                response.getOutput().getTryNext()))
        .build();
  }
}
