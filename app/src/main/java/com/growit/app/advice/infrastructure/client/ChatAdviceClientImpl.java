package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.chatadvice.service.ChatAdviceClient;
import com.growit.app.advice.usecase.dto.ai.AiChatAdviceResponse;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
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

    String fullUrl = nestApiUrl + "/advice/realtime";

    try {
      // Map to strict DTO for realtime advice to avoid sending unwanted fields
      RealtimeAdviceRequestDto realtimePayload =
          new RealtimeAdviceRequestDto(
              request.getUserId(),
              request.getGoalId(),
              request.getGoalTitle(),
              request.getConcern(),
              request.getMode(),
              request.getRecentTodos(),
              request.isGoalOnboardingCompleted());

      log.info("Chat Advice Server 실시간 조언 요청 - URL: {}, Payload: {}", fullUrl, realtimePayload);
      return webClient
          .post()
          .uri(fullUrl)
          .bodyValue(realtimePayload)
          .retrieve()
          .bodyToMono(AiChatAdviceResponse.class)
          .block();
    } catch (Exception e) {
      log.error("Chat Advice Server 실시간 조언 요청 실패 - URL: {}, Error: {}", fullUrl, e.getMessage());
      throw e;
    }
  }

  @Override
  public AiChatAdviceResponse getMorningAdvice(ChatAdviceRequest request) {
    String fullUrl = nestApiUrl + "/advice/morning";
    try {
      MorningAdviceRequestDto payload =
          new MorningAdviceRequestDto(
              request.getUserId(),
              request.getActiveGoals(),
              request.getRecentTodos(),
              request.getYesterdayConversation());

      log.info("Chat Advice Server 아침 조언 요청 - URL: {}, Payload: {}", fullUrl, payload);

      String rawResponse =
          webClient
              .post()
              .uri(fullUrl)
              .bodyValue(payload)
              .retrieve()
              .bodyToMono(String.class)
              .block();

      if (rawResponse == null) {
        throw new IllegalStateException("Received null response from Morning Advice API");
      }

      com.fasterxml.jackson.databind.ObjectMapper mapper =
          new com.fasterxml.jackson.databind.ObjectMapper();
      com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(rawResponse);

      String advice = null;

      // 1. Try finding 'advice' at root
      if (rootNode.has("advice")) {
        advice = rootNode.get("advice").asText();
      }
      // 2. Try finding 'data' -> 'advice' (NestJS Interceptor pattern)
      else if (rootNode.has("data")) {
        com.fasterxml.jackson.databind.JsonNode dataNode = rootNode.get("data");
        if (dataNode.has("advice")) {
          advice = dataNode.get("advice").asText();
        }
      }

      // 3. Fallback, try other common names
      if (advice == null && rootNode.has("message")) {
        advice = rootNode.get("message").asText();
      }

      if (advice == null) {
        log.error("Could not find 'advice' field in response: {}", rawResponse);
        // Fallback to avoid null in DB if possible, or throw
        // throw new IllegalStateException("Advice field missing in response");
        advice = "죄송해요, 아침 조언을 가져오는데 문제가 생겼어요."; // Temporary fallback
      }

      AiChatAdviceResponse result = new AiChatAdviceResponse();
      result.setSuccess(true);
      AiChatAdviceResponse.Data data = new AiChatAdviceResponse.Data();
      data.setAdvice(advice);
      data.setMode("MORNING");
      result.setData(data);

      return result;

    } catch (RuntimeException e) {
      log.error("Chat Advice Server 아침 조언 요청 실패 - URL: {}, Error: {}", fullUrl, e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Chat Advice Server 아침 조언 요청 실패 - URL: {}, Error: {}", fullUrl, e.getMessage());
      throw new RuntimeException("Failed to get morning advice", e);
    }
  }

  // Inner DTOs for Morning Advice API
  record MorningAdviceRequestDto(
      String userId,
      java.util.List<String> goalTitles,
      java.util.List<String> recentTodos,
      String previousConversations) {}

  // Inner DTO for Realtime Advice API
  record RealtimeAdviceRequestDto(
      String userId,
      String goalId,
      String goalTitle,
      String concern,
      String mode,
      java.util.List<String> recentTodos,
      boolean isGoalOnboardingCompleted) {}
}
