package com.growit.app.advice.usecase;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;
import com.growit.app.common.util.IDGenerator;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GenerateMentorAdviceUseCase {

    private final AiMentorAdviceClient aiMentorAdviceClient;

    public MentorAdvice execute(String userId, String goalId) {
        // TODO: Replace with actual data fetching logic
        AiMentorAdviceRequest.Input input = AiMentorAdviceRequest.Input.builder()
            .recentTodos(Collections.singletonList("프로젝트 기획서 작성"))
            .weeklyRetrospects(Collections.singletonList("이번 주에는 프로젝트 초기 설정에 집중했다"))
            .overallGoal("3개월 내에 사이드 프로젝트 완성하기")
            .completedTodos(Collections.singletonList("프로젝트 아이디어 구상"))
            .incompleteTodos(Collections.singletonList("프로젝트 기획서 작성"))
            .pastWeeklyGoals(Collections.singletonList("프로젝트 초기 설정"))
            .build();

        AiMentorAdviceRequest request = AiMentorAdviceRequest.builder()
            .userId(userId)
            .promptId("teamcook-advice-001")
            .input(input)
            .build();

        AiMentorAdviceResponse response = aiMentorAdviceClient.getMentorAdvice(request);

        return MentorAdvice.builder()
            .id(IDGenerator.generateId())
            .userId(userId)
            .goalId(goalId)
            .isChecked(false)
            .message("AI Mentor's Advice") // You might want a more descriptive message
            .kpt(
                new MentorAdvice.Kpt(
                    response.getOutput().getKeep(),
                    response.getOutput().getProblem(),
                    response.getOutput().getTryNext()
                )
            )
            .build();
    }
}
