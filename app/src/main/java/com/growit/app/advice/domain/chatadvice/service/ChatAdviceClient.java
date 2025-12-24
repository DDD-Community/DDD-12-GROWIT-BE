package com.growit.app.advice.domain.chatadvice.service;

import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;

public interface ChatAdviceClient {
  AiChatAdviceResponse getRealtimeAdvice(ChatAdviceRequest request);
}
