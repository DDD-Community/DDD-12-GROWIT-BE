package com.growit.app.mentor.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.mentor.domain.vo.IntimacyLevel;
import com.growit.app.mentor.usecase.GetMentorIntimacyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
class MentorControllerTest {

  private MockMvc mockMvc;

  @MockitoBean private GetMentorIntimacyUseCase getMentorIntimacyUseCase;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    TestSecurityUtil.setMockUser();
  }

  @Test
  void getMentorIntimacy() throws Exception {
    // given
    given(getMentorIntimacyUseCase.execute("user-1")).willReturn(IntimacyLevel.HIGH);

    // when & then
    mockMvc
        .perform(get("/mentor/intimacy").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andExpect(jsonPath("$.data").value("상"))
        .andDo(
            document(
                "get-mentor-intimacy",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Mentor")
                        .summary("멘토 친밀도 조회")
                        .description("사용자의 투두 개수와 주간 회고 개수를 기반으로 멘토 친밀도를 조회합니다.")
                        .requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("JWT 토큰"))
                        .responseFields(
                            subsectionWithPath("data").description("친밀도 레벨 (상, 중, 하)"))
                        .build())));
  }
}
