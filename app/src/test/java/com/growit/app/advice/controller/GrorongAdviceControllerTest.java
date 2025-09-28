package com.growit.app.advice.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.advice.controller.AdviceDocumentFields;
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
  private static final String TAG = AdviceDocumentFields.TAG;

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
                        .tag(TAG)
                        .summary("그로롱 조언 조회")
                        .description("사용자 상태에 따른 그로롱의 격려 메시지와 명언을 조회합니다.")
                        .responseFields(AdviceDocumentFields.GRORONG_ADVICE_RESPONSE_FIELDS)
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
                        .tag(TAG)
                        .summary("멘토 조언 조회")
                        .responseFields(AdviceDocumentFields.MENTOR_ADVICE_RESPONSE_FIELDS)
                        .build())));
  }
}
