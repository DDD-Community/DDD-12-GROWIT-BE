package com.growit.app.advice.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.BOOLEAN;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.advice.controller.dto.response.GrorongAdviceResponse;
import com.growit.app.advice.controller.dto.response.MentorAdviceResponse;
import com.growit.app.advice.controller.mapper.GrorongAdviceResponseMapper;
import com.growit.app.advice.controller.mapper.MentorAdviceResponseMapper;
import com.growit.app.advice.domain.grorong.Grorong;
import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.advice.usecase.GetGrorongAdviceUseCase;
import com.growit.app.advice.usecase.GetMentorAdviceUseCase;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.fake.advice.GrorongFixture;
import com.growit.app.fake.advice.MentorAdviceFixture;
import com.growit.app.user.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
class GrorongAdviceControllerTest {
  private MockMvc mockMvc;

  @MockitoBean private GetGrorongAdviceUseCase getGrorongAdviceUseCase;
  @MockitoBean private GetMentorAdviceUseCase getMentorAdviceUseCase;
  @MockitoBean private GrorongAdviceResponseMapper grorongAdviceResponseMapper;
  @MockitoBean private MentorAdviceResponseMapper mentorAdviceResponseMapper;

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
  void getGrorongAdviceSuccess() throws Exception {
    // given
    Grorong grorong = GrorongFixture.defaultGrorong();
    GrorongAdviceResponse mockResponse = GrorongFixture.defaultGrorongAdviceResponse();

    given(getGrorongAdviceUseCase.execute(any(String.class))).willReturn(grorong);
    given(grorongAdviceResponseMapper.toResponse(grorong)).willReturn(mockResponse);

    // when & then
    mockMvc
        .perform(get("/advice/grorong").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-grorong-advice",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Advice")
                        .summary("그로롱 조언 조회")
                        .description("사용자 상태에 따른 그로롱의 격려 메시지와 명언을 조회합니다.")
                        .responseFields(
                            fieldWithPath("data.saying").type(STRING).description("그로롱이 전하는 명언"),
                            fieldWithPath("data.message")
                                .type(STRING)
                                .description("현재 기분에 따른 격려 메시지"),
                            fieldWithPath("data.mood")
                                .type(STRING)
                                .description("현재 기분 상태 (HAPPY, NORMAL, SAD)"))
                        .build())));
  }

  @Test
  void getMentorAdviceSuccess() throws Exception {
    // given
    MentorAdvice mentorAdvice = MentorAdviceFixture.checkedMentorAdvice();
    MentorAdviceResponse mockResponse = MentorAdviceFixture.defaultMentorAdviceResponse();

    given(getMentorAdviceUseCase.execute(any(User.class))).willReturn(mentorAdvice);
    given(mentorAdviceResponseMapper.toResponse(mentorAdvice)).willReturn(mockResponse);

    // when & then
    mockMvc
        .perform(get("/advice/mentor").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-mentor-advice",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Advice")
                        .summary("멘토 조언 조회")
                        .responseFields(
                            fieldWithPath("data.isChecked").type(BOOLEAN).description("조언 확인 여부"),
                            fieldWithPath("data.message").type(STRING).description("멘토 조언 메시지"),
                            fieldWithPath("data.kpt").description("KPT 피드백 객체"),
                            fieldWithPath("data.kpt.keep").type(STRING).description("Keep - 계속할 것"),
                            fieldWithPath("data.kpt.problem")
                                .type(STRING)
                                .description("Problem - 문제점"),
                            fieldWithPath("data.kpt.tryNext")
                                .type(STRING)
                                .description("Try - 시도할 것"))
                        .build())));
  }
}
