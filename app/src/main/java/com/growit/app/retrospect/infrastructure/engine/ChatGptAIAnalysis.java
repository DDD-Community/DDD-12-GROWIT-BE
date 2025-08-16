package com.growit.app.retrospect.infrastructure.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growit.app.common.config.ai.AIProperties;
import com.growit.app.common.exception.BadRequestException;
import com.growit.app.retrospect.domain.goalretrospect.service.AIAnalysis;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.infrastructure.engine.dto.AnalysisDto;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ChatGptAIAnalysis implements AIAnalysis {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  private final RestTemplate restTemplate;
  private final AIProperties aiProperties;


  private static final String SYSTEM_MESSAGE =
    """
    당신은 목표 달성을 위해 미루지 않고 끝까지 달성할 수 있도록 돕는 전문가입니다.
    사용자가 제공한 JSON 데이터(goal, retrospects, todos)를 바탕으로 아래 기준에 맞추어 분석하고 요약하세요.

    summary:
    [조건]
    - 100자 이상
    - 문장 어미는 "~니다"로 통일
    - 2~3문장 구성
    [내용]
    - 첫 문장은 Copywriting이 될만한 핵심적인 목표 전반 요약
    - 이어서 세부적인 목표 진행 과정과 맥락 서술

    advice:
    [조건]
    - 100자 이상
    - 문장 어미는 "~다냥"으로 통일
    - 2~3문장 구성
    [내용]
    - 목표 진행 과정에서 잘한 점(강점) 분석
    - 개선해야 할 점(약점) 분석
    - 사례와 근거 기반
    - 문장을 깔끔하고 가독성 있게 구성

    출력 포맷(JSON):
    {
      "summary": "...",
      "advice": "..."
    }
    """;

  @Override
  public Analysis generate(AnalysisDto request) {
    try {
      // 1) 사용자 메시지에 실제 JSON 데이터 삽입
      Map<String, Object> payload = new HashMap<>();
      payload.put("goal", request.goal());
      payload.put("retrospects", request.retrospects());
      payload.put("todos", request.todos());
      String userInput =
        "goal = "
          + objectMapper.writeValueAsString(payload.get("goal"))
          + "\n"
          + "retrospects = "
          + objectMapper.writeValueAsString(payload.get("retrospects"))
          + "\n"
          + "todos = "
          + objectMapper.writeValueAsString(payload.get("todos"))
          + "\n\n"
          + "위 데이터를 기반으로 summary와 advice를 작성해주세요.";

      // 2) Chat Completions 요청 바디 구성
      Map<String, Object> sysMsg = Map.of("role", "system", "content", SYSTEM_MESSAGE);
      Map<String, Object> usrMsg = Map.of("role", "user", "content", userInput);
      Map<String, Object> body = new HashMap<>();
      body.put("model", aiProperties.getModel());
      body.put("temperature", aiProperties.getTemperature());
      body.put("messages", List.of(sysMsg, usrMsg));

      // 3) 호출 using RestTemplate
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(aiProperties.getApiKey());

      HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

      ResponseEntity<String> responseEntity =
        restTemplate.exchange(
            aiProperties.getBaseUrl() + "/v1/chat/completions", HttpMethod.POST, entity, String.class);

      String response = responseEntity.getBody();

      if (response == null) {
        throw new BadRequestException("OpenAI 응답이 비어 있습니다.");
      }

      // 4) 응답 파싱 (choices[0].message.content)
      JsonNode root = objectMapper.readTree(response);
      JsonNode choices = root.path("choices");
      if (!choices.isArray() || choices.isEmpty()) {
        throw new BadRequestException("OpenAI 응답에 choices가 없습니다: " + response);
      }
      String content = choices.get(0).path("message").path("content").asText("");
      if (content.isEmpty()) {
        throw new BadRequestException("OpenAI 응답에 content가 없습니다: " + response);
      }

      // 5) 모델이 반환한 JSON 텍스트 파싱
      // 불필요한 앞뒤 공백/코드펜스 제거
      String cleaned = content.replace("```json", "").replace("```", "").trim();

      JsonNode json = objectMapper.readTree(cleaned.getBytes(StandardCharsets.UTF_8));
      String summary = json.path("summary").asText("");
      String advice = json.path("advice").asText("");

      if (summary.isEmpty() || advice.isEmpty()) {
        throw new IllegalStateException("요청 포맷(JSON)에 맞는 summary/advice가 없습니다: " + cleaned);
      }

      return new Analysis(summary, advice);
    } catch (Exception e) {
      // 실패 시, 에러 메시지를 포함한 최소 응답 반환
      String fallbackSummary = "AI 분석을 생성하는 중 오류가 발생했습니다. 시스템 로그를 확인해 주십시오.";
      String fallbackAdvice = "예외: " + e.getMessage() + " 다냥";
      return new Analysis(fallbackSummary, fallbackAdvice);
    }
  }
}

