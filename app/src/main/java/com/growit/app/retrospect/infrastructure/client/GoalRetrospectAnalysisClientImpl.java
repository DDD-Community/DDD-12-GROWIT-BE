package com.growit.app.retrospect.infrastructure.client;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.retrospect.domain.goalretrospect.service.AIAnalysis;
import com.growit.app.retrospect.domain.goalretrospect.vo.Analysis;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.infrastructure.engine.dto.AnalysisDto;
import com.growit.app.retrospect.usecase.dto.ai.GoalRetrospectAnalysisRequest;
import com.growit.app.retrospect.usecase.dto.ai.GoalRetrospectAnalysisResponse;
import com.growit.app.todo.domain.ToDo;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class GoalRetrospectAnalysisClientImpl implements AIAnalysis {

  private final WebClient webClient;

  @Value("${ai.mentor.url}")
  private String nestApiUrl;

  @PostConstruct
  public void init() {
    if (nestApiUrl != null) {
      this.nestApiUrl = this.nestApiUrl.trim();
      log.info("Goal Retrospect Analysis Server URL 초기화: '{}'", nestApiUrl);
    } else {
      log.warn("Goal Retrospect Analysis Server URL이 설정되지 않았습니다.");
    }
  }

  @Override
  public Analysis generate(AnalysisDto request) {
    String fullUrl = nestApiUrl + "/goal-retrospect/analyze";

    try {
      GoalRetrospectAnalysisRequest nestRequest = convertToNestRequest(request);
      if (nestRequest == null) {
        log.error("NestJS 요청 변환 실패: request가 null입니다.");
        throw new IllegalArgumentException("NestJS 요청 변환 실패: request가 null입니다.");
      }
      GoalRetrospectAnalysisResponse response =
          webClient
              .post()
              .uri(fullUrl)
              .bodyValue(nestRequest)
              .retrieve()
              .onStatus(
                  status -> status.is4xxClientError() || status.is5xxServerError(),
                  clientResponse -> {
                    return clientResponse
                        .bodyToMono(String.class)
                        .defaultIfEmpty("(응답 본문 없음)")
                        .flatMap(
                            responseBody -> {
                              log.error(
                                  "AI 분석 요청 실패 - URL: {}, Status: {}, Response: {}",
                                  fullUrl,
                                  clientResponse.statusCode(),
                                  responseBody);
                              return clientResponse
                                  .createException()
                                  .map(
                                      ex ->
                                          new RuntimeException(
                                              "NestJS 서비스 에러: "
                                                  + clientResponse.statusCode()
                                                  + " - "
                                                  + ex.getMessage(),
                                              ex));
                            });
                  })
              .bodyToMono(GoalRetrospectAnalysisResponse.class)
              .block(Duration.ofMinutes(5));

      if (response == null || !response.isSuccess()) {
        log.error("AI 분석 응답 실패 - URL: {}, Response: {}", fullUrl, response);
        throw new RuntimeException("NestJS AI 서비스 응답이 실패했습니다.");
      }

      return new Analysis(response.getAnalysis().getSummary(), response.getAnalysis().getAdvice());

    } catch (WebClientResponseException e) {
      log.error(
          "AI 분석 HTTP 에러 - URL: {}, Status: {}, Response: {}",
          fullUrl,
          e.getStatusCode(),
          e.getResponseBodyAsString());
      String fallbackSummary =
          "AI 분석을 생성하는 중 오류가 발생했습니다. (HTTP " + e.getStatusCode() + ") 시스템 로그를 확인해 주십시오.";
      String fallbackAdvice = "예외: " + e.getMessage() + " 다냥";
      return new Analysis(fallbackSummary, fallbackAdvice);
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      if (errorMessage != null && errorMessage.contains("Connection refused")) {
        log.error("AI 분석 연결 실패 - NestJS 서버가 실행 중인지 확인하세요. URL: {}", nestApiUrl);
      } else {
        log.error("AI 분석 요청 실패 - URL: {}, Error: {}", fullUrl, errorMessage, e);
      }
      String fallbackSummary = "AI 분석을 생성하는 중 오류가 발생했습니다. 시스템 로그를 확인해 주십시오.";
      String fallbackAdvice = "예외: " + errorMessage + " 다냥";
      return new Analysis(fallbackSummary, fallbackAdvice);
    }
  }

  private GoalRetrospectAnalysisRequest convertToNestRequest(AnalysisDto request) {
    Goal goal = request.goal();
    List<Retrospect> retrospects = request.retrospects();
    List<ToDo> todos = request.todos();

    // Goal 변환
    GoalRetrospectAnalysisRequest.GoalDto goalDto =
        GoalRetrospectAnalysisRequest.GoalDto.builder()
            .id(goal.getId())
            .title(goal.getName())
            .description(goal.getName()) // getToBe() 메서드가 제거됨, 목표명으로 대체
            .build();

    // Retrospects 변환
    List<GoalRetrospectAnalysisRequest.RetrospectDto> retrospectDtos =
        retrospects.stream()
            .map(
                retrospect -> {
                  GoalRetrospectAnalysisRequest.KptDto kptDto = null;
                  if (retrospect.getKpt() != null) {
                    kptDto =
                        GoalRetrospectAnalysisRequest.KptDto.builder()
                            .keep(retrospect.getKpt().keep())
                            .problem(retrospect.getKpt().problem())
                            .tryNext(retrospect.getKpt().tryNext())
                            .build();
                  }

                  // Retrospect의 content는 KPT를 기반으로 생성
                  String content = "";
                  if (retrospect.getKpt() != null) {
                    content =
                        String.format(
                            "Keep: %s, Problem: %s, Try: %s",
                            retrospect.getKpt().keep(),
                            retrospect.getKpt().problem(),
                            retrospect.getKpt().tryNext());
                  }

                  return GoalRetrospectAnalysisRequest.RetrospectDto.builder()
                      .id(retrospect.getId())
                      .content(content)
                      .kpt(kptDto)
                      .build();
                })
            .toList();

    // Todos 변환
    List<GoalRetrospectAnalysisRequest.TodoDto> todoDtos =
        todos.stream()
            .filter(todo -> !todo.isDeleted())
            .map(
                todo ->
                    GoalRetrospectAnalysisRequest.TodoDto.builder()
                        .id(todo.getId())
                        .title(todo.getContent())
                        .completed(todo.isCompleted())
                        .build())
            .toList();

    return GoalRetrospectAnalysisRequest.builder()
        .goalId(goal.getId())
        .content("") // 빈 문자열로 설정 (NestJS에서 허용하도록 수정 필요)
        .goal(goalDto)
        .retrospects(retrospectDtos)
        .todos(todoDtos)
        .build();
  }
}
