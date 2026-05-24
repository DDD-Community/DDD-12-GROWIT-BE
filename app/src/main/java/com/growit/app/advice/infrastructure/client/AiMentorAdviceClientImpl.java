package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.exception.AiServiceDisabledException;
import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationRequest;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationResponse;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;
import org.springframework.stereotype.Component;

@Component
public class AiMentorAdviceClientImpl implements AiMentorAdviceClient {

  @Override
  public AiMentorAdviceResponse getMentorAdvice(AiMentorAdviceRequest request) {
    throw new AiServiceDisabledException();
  }

  @Override
  public AiGoalRecommendationResponse getGoalRecommendation(AiGoalRecommendationRequest request) {
    throw new AiServiceDisabledException();
  }
}
