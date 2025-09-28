package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.mentor.service.AiMentorAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiMentorAdviceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AiMentorAdviceClientImpl implements AiMentorAdviceClient {

  private final WebClient webClient;

  @Value("${ai.mentor.url}")
  private String nestApiUrl;

  @Override
  public AiMentorAdviceResponse getMentorAdvice(AiMentorAdviceRequest request) {
    return webClient
        .post()
        .uri(nestApiUrl + "/daily-advice")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(AiMentorAdviceResponse.class)
        .block();
  }
}
