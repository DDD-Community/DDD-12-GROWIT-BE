package com.growit.app.advice.domain.chatadvice.service;

import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;

public interface ChatAdviceClient {
  AiChatAdviceResponse getRealtimeAdvice(ChatAdviceRequest request);

  AiChatAdviceResponse getMorningAdvice(ChatAdviceRequest request);
}
