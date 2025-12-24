package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.chatadvice.service.ChatAdviceClient;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatAdviceClientImpl implements ChatAdviceClient {

  private final WebClient webClient;

  @Value("${ai.mentor.url}")
  private String nestApiUrl;

  @PostConstruct
  public void init() {
    if (nestApiUrl != null) {
      this.nestApiUrl = this.nestApiUrl.trim();
      log.info("Chat Advice Server URL 초기화: '{}'", nestApiUrl);
    } else {
      log.warn("Chat Advice Server URL이 설정되지 않았습니다.");
    }
  }

  @Override
  public AiChatAdviceResponse getRealtimeAdvice(ChatAdviceRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("ChatAdviceRequest cannot be null");
    }
    String fullUrl = nestApiUrl + "/api/advice/realtime";

    try {
      log.info("Chat Advice Server 실시간 조언 요청 - URL: {}, Request: {}", fullUrl, request);
      return webClient
          .post()
          .uri(fullUrl)
          .bodyValue(request)
          .retrieve()
          .bodyToMono(AiChatAdviceResponse.class)
          .block();
    } catch (Exception e) {
      log.error(
          "Chat Advice Server 실시간 조언 요청 실패 - URL: {}, Error: {}", fullUrl, e.getMessage());
      throw e;
    }
  }

}

