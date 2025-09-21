package com.growit.app.ai.infrastructure.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.ai.domain.event.AIAdviceRequestEvent;
import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import com.growit.app.ai.domain.event.AIPlanRecommendationRequestEvent;
import com.growit.app.ai.domain.event.AIPlanRecommendationResponseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ai.service.base-url:http://localhost:8001}")
    private String aiServiceBaseUrl;

    @Value("${ai.service.mock-enabled:true}")
    private boolean mockEnabled;

    /**
     * 오늘의 조언 생성 API 호출
     */
    public AIAdviceResponseEvent generateAdvice(AIAdviceRequestEvent requestEvent) {
        // Mock 모드인 경우 Mock 응답 반환
        if (mockEnabled) {
            return createMockAdviceResponse(requestEvent);
        }

        try {
            String url = aiServiceBaseUrl + "/api/daily-advice";

            // NestJS API 요청 형식으로 변환
            GenerateAdviceRequest request = GenerateAdviceRequest.builder()
                    .userId(requestEvent.getUserId())
                    .promptId(requestEvent.getPromptId())
                    .templateUid(requestEvent.getTemplateUid())
                    .input(GenerateAdviceInput.builder()
                            .mentorType(requestEvent.getMentorType())
                            .recentTodos(requestEvent.getRecentTodos())
                            .weeklyRetrospects(requestEvent.getWeeklyRetrospects())
                            .overallGoal(requestEvent.getOverallGoal())
                            .build())
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GenerateAdviceRequest> entity = new HttpEntity<>(request, headers);

            log.info("Calling AI service for advice generation: {}", url);
            ResponseEntity<GenerateAdviceResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, GenerateAdviceResponse.class);

            GenerateAdviceResponse responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("AI service returned null response");
            }

            // 응답을 AIAdviceResponseEvent로 변환
            return AIAdviceResponseEvent.builder()
                    .success(responseBody.isSuccess())
                    .userId(responseBody.getUserId())
                    .goalMentorId(requestEvent.getGoalMentorId())
                    .adviceId(responseBody.getId())
                    .output(responseBody.getOutput() != null ?
                            AIAdviceResponseEvent.AdviceOutput.builder()
                                    .keeps(responseBody.getOutput().getKeeps())
                                    .problems(responseBody.getOutput().getProblems())
                                    .trys(responseBody.getOutput().getTrys())
                                    .build() : null)
                    .generatedAt(responseBody.getGeneratedAt() != null ?
                            responseBody.getGeneratedAt().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() :
                            LocalDateTime.now())
                    .error(responseBody.getError())
                    .build();

        } catch (Exception e) {
            log.error("Failed to call AI service for advice generation", e);
            return AIAdviceResponseEvent.builder()
                    .success(false)
                    .userId(requestEvent.getUserId())
                    .goalMentorId(requestEvent.getGoalMentorId())
                    .error(e.getMessage())
                    .generatedAt(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 목표 추천 생성 API 호출
     */
    public AIPlanRecommendationResponseEvent generatePlanRecommendation(AIPlanRecommendationRequestEvent requestEvent) {
        // Mock 모드인 경우 Mock 응답 반환
        if (mockEnabled) {
            return createMockPlanRecommendationResponse(requestEvent);
        }

        try {
            String url = aiServiceBaseUrl + "/api/goal-recommendation";

            // NestJS API 요청 형식으로 변환
            GenerateGoalRecommendationRequest request = GenerateGoalRecommendationRequest.builder()
                    .userId(requestEvent.getUserId())
                    .promptId(requestEvent.getPromptId())
                    .templateUid(requestEvent.getTemplateUid())
                    .input(GenerateGoalRecommendationInput.builder()
                            .pastTodos(requestEvent.getRecentProgress().isEmpty() ? 
                                List.of("테스트 투두 1", "테스트 투두 2") : requestEvent.getRecentProgress())
                            .pastRetrospects(List.of("테스트 회고 1", "테스트 회고 2")) // TODO: 실제 회고 데이터 조회
                            .overallGoal(requestEvent.getGoalName())
                            .completedTodos(List.of("완료된 투두 1")) // TODO: 완료된 투두 조회
                            .pastWeeklyGoals(List.of("과거 주간 목표 1")) // TODO: 과거 주간 목표 조회
                            .remainingTime("1주") // TODO: 실제 남은 시간 계산
                            .build())
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GenerateGoalRecommendationRequest> entity = new HttpEntity<>(request, headers);

            log.info("Calling AI service for plan recommendation: {}", url);
            ResponseEntity<GenerateGoalRecommendationResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, GenerateGoalRecommendationResponse.class);

            GenerateGoalRecommendationResponse responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("AI service returned null response");
            }

            // 응답을 AIPlanRecommendationResponseEvent로 변환
            return AIPlanRecommendationResponseEvent.builder()
                    .success(responseBody.isSuccess())
                    .userId(responseBody.getUserId())
                    .goalId(requestEvent.getGoalId())
                    .planId(requestEvent.getPlanId())
                    .output(responseBody.getOutput())
                    .generatedAt(responseBody.getGeneratedAt() != null ?
                            responseBody.getGeneratedAt().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() :
                            LocalDateTime.now())
                    .error(responseBody.getError())
                    .build();

        } catch (Exception e) {
            log.error("Failed to call AI service for plan recommendation", e);
            return AIPlanRecommendationResponseEvent.builder()
                    .success(false)
                    .userId(requestEvent.getUserId())
                    .goalId(requestEvent.getGoalId())
                    .planId(requestEvent.getPlanId())
                    .error(e.getMessage())
                    .generatedAt(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * Mock 응답 생성 (AI 서비스 없이 테스트용)
     */
    private AIAdviceResponseEvent createMockAdviceResponse(AIAdviceRequestEvent requestEvent) {
        log.info("Using mock response for AI advice generation");

        return AIAdviceResponseEvent.builder()
                .success(true)
                .userId(requestEvent.getUserId())
                .goalMentorId(requestEvent.getGoalMentorId())
                .adviceId("mock-advice-" + System.currentTimeMillis())
                .output(AIAdviceResponseEvent.AdviceOutput.builder()
                        .keeps("오늘도 꾸준히 목표를 향해 나아가고 있습니다.")
                        .problems("시간 관리가 아직 부족한 것 같습니다.")
                        .trys("내일은 시간을 더 체계적으로 관리해보겠습니다.")
                        .build())
                .generatedAt(LocalDateTime.now())
                .build();
    }

    private AIPlanRecommendationResponseEvent createMockPlanRecommendationResponse(AIPlanRecommendationRequestEvent requestEvent) {
        log.info("Using mock response for AI plan recommendation");

        return AIPlanRecommendationResponseEvent.builder()
                .success(true)
                .userId(requestEvent.getUserId())
                .goalId(requestEvent.getGoalId())
                .planId(requestEvent.getPlanId())
                .output("이번 주에는 더 구체적인 계획을 세워보세요. 작은 단위로 나누어 진행하면 좋을 것 같습니다.")
                .generatedAt(LocalDateTime.now())
                .build();
    }

    // NestJS API 요청/응답 DTO들
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateAdviceRequest {
        private String userId;
        private String promptId;
        private String templateUid;
        private GenerateAdviceInput input;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateAdviceInput {
        private String mentorType;
        private List<String> recentTodos;
        private List<String> weeklyRetrospects;
        private String overallGoal;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateAdviceResponse {
        private boolean success;
        private String userId;
        private String promptId;
        private String id;
        private AdviceOutput output;
        private java.util.Date generatedAt;
        private String error;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AdviceOutput {
            private String keeps;
            private String problems;
            @JsonProperty("try")
            private String trys;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateGoalRecommendationRequest {
        private String userId;
        private String promptId;
        private String templateUid;
        private GenerateGoalRecommendationInput input;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateGoalRecommendationInput {
        private List<String> pastTodos;
        private List<String> pastRetrospects;
        private String overallGoal;
        private List<String> completedTodos;
        private List<String> pastWeeklyGoals;
        private String remainingTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateGoalRecommendationResponse {
        private boolean success;
        private String userId;
        private String promptId;
        private String id;
        private String output;
        private java.util.Date generatedAt;
        private String error;
    }
}
