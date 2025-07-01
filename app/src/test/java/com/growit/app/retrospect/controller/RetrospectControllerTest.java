package com.growit.app.retrospect.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.usecase.CreateRetrospectUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class RetrospectControllerTest {
  private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private CreateRetrospectUseCase createRetrospectUseCase;

  @BeforeEach
  void setUp(
      WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentationContextProvider) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
            .build();
    TestSecurityUtil.setMockUser();
  }

  @Test
  void 회고_생성_성공() throws Exception {
    // given
    String retrospectId = "retrospect-id-123";
    given(createRetrospectUseCase.execute(any())).willReturn(retrospectId);

    CreateRetrospectRequest request = new CreateRetrospectRequest(
        "goal-id-123",
        "plan-id-123",
        "이번 주는 목표를 잘 달성한 것 같습니다. 다음 주에는 더 열심히 하겠습니다."
    );

    // when & then
    mockMvc.perform(post("/retrospects")
            .header("Authorization", "Bearer mock-jwt-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").value(retrospectId));
  }

  @Test
  void 회고_생성_실패_내용_누락() throws Exception {
    // given
    CreateRetrospectRequest request = new CreateRetrospectRequest(
        "goal-id-123",
        "plan-id-123",
        ""  // 빈 내용
    );

    // when & then
    mockMvc.perform(post("/retrospects")
            .header("Authorization", "Bearer mock-jwt-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 회고_생성_실패_내용_너무_짧음() throws Exception {
    // given
    CreateRetrospectRequest request = new CreateRetrospectRequest(
        "goal-id-123",
        "plan-id-123",
        "짧음"  // 10자 미만
    );

    // when & then
    mockMvc.perform(post("/retrospects")
            .header("Authorization", "Bearer mock-jwt-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}