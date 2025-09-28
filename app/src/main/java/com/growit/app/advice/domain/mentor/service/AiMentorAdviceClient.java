package com.growit.app.advice.domain.mentor.service;

import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationRequest;
import com.growit.app.advice.usecase.dto.ai.AiGoalRecommendationResponse;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;

public interface AiMentorAdviceClient {

  AiMentorAdviceResponse getMentorAdvice(AiMentorAdviceRequest request);

  AiGoalRecommendationResponse getGoalRecommendation(AiGoalRecommendationRequest request);
}
