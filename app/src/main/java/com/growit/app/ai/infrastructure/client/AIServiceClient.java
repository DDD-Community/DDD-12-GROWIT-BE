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


    public AIAdviceResponseEvent generateAdvice(AIAdviceRequestEvent requestEvent) {

        try {
            String url = aiServiceBaseUrl + "/api/daily-advice";

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

            ResponseEntity<GenerateAdviceResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, GenerateAdviceResponse.class);

            GenerateAdviceResponse responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("AI service returned null response");
            }


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

    public AIPlanRecommendationResponseEvent generatePlanRecommendation(AIPlanRecommendationRequestEvent requestEvent) {

        try {
            String url = aiServiceBaseUrl + "/api/goal-recommendation";

            GenerateGoalRecommendationRequest request = GenerateGoalRecommendationRequest.builder()
                    .userId(requestEvent.getUserId())
                    .promptId(requestEvent.getPromptId())
                    .templateUid(requestEvent.getTemplateUid())
                    .input(GenerateGoalRecommendationInput.builder()
                            .pastTodos(requestEvent.getRecentProgress())
                            .pastRetrospects(List.of())
                            .overallGoal(requestEvent.getGoalName())
                            .completedTodos(List.of())
                            .pastWeeklyGoals(List.of())
                            .remainingTime("1주")
                            .build())
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GenerateGoalRecommendationRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<GenerateGoalRecommendationResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, GenerateGoalRecommendationResponse.class);

            GenerateGoalRecommendationResponse responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("AI service returned null response");
            }

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
            @JsonProperty("keep")
            private String keeps;
            @JsonProperty("problem")
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
