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

    return AiMentorAdviceRequest.builder()
        .userId(user.getId())
        .promptId(goal.getMentor().getAdvicePromptId())
        .input(input)
        .build();
  }

  private MentorAdvice createMentorAdvice(User user, Goal goal, AiMentorAdviceResponse response) {
    return MentorAdvice.builder()
        .id(IDGenerator.generateId())
        .userId(user.getId())
        .goalId(goal.getId())
        .isChecked(false)
        .message("AI Mentor's Advice")
        .kpt(
            new MentorAdvice.Kpt(
                response.getOutput().getKeep(),
                response.getOutput().getProblem(),
                response.getOutput().getTryNext()))
        .build();
  }
}
