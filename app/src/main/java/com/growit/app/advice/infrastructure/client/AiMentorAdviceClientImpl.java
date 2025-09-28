package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class AiMentorAdviceClientImpl implements AiMentorAdviceClient {

    @Override
    public AiMentorAdviceResponse getMentorAdvice(AiMentorAdviceRequest request) {
        // This is a mock implementation that returns a hardcoded response for testing.
        return createMockResponse(request.getUserId(), request.getPromptId());
    }

    private AiMentorAdviceResponse createMockResponse(String userId, String promptId) {
        AiMentorAdviceResponse mockResponse = new AiMentorAdviceResponse();
        mockResponse.setSuccess(true);
        mockResponse.setUserId(userId);
        mockResponse.setPromptId(promptId);
        mockResponse.setId("mock-response-id");
        mockResponse.setGeneratedAt(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

        AiMentorAdviceResponse.Output output = new AiMentorAdviceResponse.Output();
        output.setKeep("Mock Keep: 꾸준히 진행하는 모습이 보기 좋습니다.");
        output.setProblem("Mock Problem: 목표 달성을 위한 구체적인 계획이 부족해 보입니다.");
        output.setTryNext("Mock Try: 주간 계획을 더 작은 단위로 나누어 실천해 보세요.");
        
        mockResponse.setOutput(output);

        return mockResponse;
    }
}
