package com.growit.app.advice.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.advice.controller.dto.request.SendChatAdviceRequest;
import com.growit.app.advice.controller.dto.response.ChatAdviceResponse;
import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import com.growit.app.advice.usecase.GetChatAdviceUseCase;
import com.growit.app.advice.usecase.SendChatAdviceUseCase;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.user.domain.user.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ChatAdviceControllerTest {

  private MockMvc mockMvc;

  @MockitoBean private GetChatAdviceUseCase getChatAdviceUseCase;
  @MockitoBean private SendChatAdviceUseCase sendChatAdviceUseCase;

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    TestSecurityUtil.setMockUser();
  }

  @Test
  void getChatAdviceStatusTest() throws Exception {
    // given
    ChatAdviceResponse response =
        ChatAdviceResponse.builder()
            .remainingCount(3)
            .isGoalOnboardingCompleted(false)
            .conversations(
                List.of(
                    new ChatAdviceResponse.ConversationResponse(
                        "계기가 뭐야?", "그로롱 답변", LocalDateTime.now())))
            .build();

    given(getChatAdviceUseCase.execute(any(User.class), anyInt())).willReturn(response);

    // when & then
    mockMvc
        .perform(
            get("/advice/chat").param("week", "1").header("Authorization", "Bearer mock-token"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-chat-advice-status",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Advice")
                        .summary("실시간 채팅 조언 상태 조회")
                        .description("사용자의 남은 대화 횟수와 온보딩 완료 여부, 대화 내역을 조회합니다.")
                        .queryParameters(
                            org.springframework.restdocs.request.RequestDocumentation
                                .parameterWithName("week")
                                .description("조회할 주차"))
                        .responseFields(
                            fieldWithPath("data.remainingCount")
                                .type(NUMBER)
                                .description("남은 대화 횟수"),
                            fieldWithPath("data.isGoalOnboardingCompleted")
                                .type(BOOLEAN)
                                .description("온보딩 완료 여부"),
                            fieldWithPath("data.conversations")
                                .type(ARRAY)
                                .description("대화 내역 리스트"),
                            fieldWithPath("data.conversations[].userMessage")
                                .type(STRING)
                                .description("사용자 메시지"),
                            fieldWithPath("data.conversations[].grorongResponse")
                                .type(STRING)
                                .description("그로롱 답변"),
                            fieldWithPath("data.conversations[].timestamp")
                                .type(STRING)
                                .description("시간"))
                        .build())));
  }

  @Test
  void sendChatAdviceTest() throws Exception {
    // given
    SendChatAdviceRequest request =
        new SendChatAdviceRequest(1, "목표 달성 힘드네", "goal-1", AdviceStyle.STRATEGIC, true);

    ChatAdviceResponse response =
        ChatAdviceResponse.builder()
            .remainingCount(2)
            .isGoalOnboardingCompleted(true)
            .conversations(
                List.of(
                    new ChatAdviceResponse.ConversationResponse(
                        "목표 달성 힘드네", "전략적인 답변", LocalDateTime.now())))
            .build();

    given(sendChatAdviceUseCase.execute(any(), anyInt(), anyString(), anyString(), any(), any()))
        .willReturn(response);

    // when & then
    mockMvc
        .perform(
            post("/advice/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer mock-token"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "send-chat-advice",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Advice")
                        .summary("실시간 채팅 조언 전송")
                        .description("그로롱에게 고민을 보내고 조언을 받습니다.")
                        .requestFields(
                            fieldWithPath("week").type(NUMBER).description("현재 주차"),
                            fieldWithPath("userMessage")
                                .type(STRING)
                                .description("사용자 메시지 (5-100자)"),
                            fieldWithPath("goalId").type(STRING).description("목표 ID"),
                            fieldWithPath("adviceStyle")
                                .type(STRING)
                                .description("조언 스타일 (BASIC, WARM, FACTUAL, STRATEGIC)"),
                            fieldWithPath("isOnboarding")
                                .type(BOOLEAN)
                                .optional()
                                .description("온보딩 답변 여부"))
                        .responseFields(
                            fieldWithPath("data.remainingCount")
                                .type(NUMBER)
                                .description("남은 대화 횟수"),
                            fieldWithPath("data.isGoalOnboardingCompleted")
                                .type(BOOLEAN)
                                .description("온보딩 완료 여부"),
                            fieldWithPath("data.conversations")
                                .type(ARRAY)
                                .description("대화 내역 리스트"),
                            fieldWithPath("data.conversations[].userMessage")
                                .type(STRING)
                                .description("사용자 메시지"),
                            fieldWithPath("data.conversations[].grorongResponse")
                                .type(STRING)
                                .description("그로롱 답변"),
                            fieldWithPath("data.conversations[].timestamp")
                                .type(STRING)
                                .description("시간"))
                        .build())));
  }
}
