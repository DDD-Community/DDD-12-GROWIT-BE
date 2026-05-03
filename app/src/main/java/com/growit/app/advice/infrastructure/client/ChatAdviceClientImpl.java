package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.chatadvice.service.ChatAdviceClient;
import com.growit.app.advice.domain.exception.AiServiceDisabledException;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import org.springframework.stereotype.Component;

@Component
public class ChatAdviceClientImpl implements ChatAdviceClient {

  @Override
  public AiChatAdviceResponse getRealtimeAdvice(ChatAdviceRequest request) {
    throw new AiServiceDisabledException();
  }

  @Override
  public AiChatAdviceResponse getMorningAdvice(ChatAdviceRequest request) {
    throw new AiServiceDisabledException();
  }
}
