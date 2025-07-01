package com.growit.retrospect.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.common.TestSecurityUtil;
import com.growit.fake.goal.FakeGoalRepository;
import com.growit.fake.goal.FakeGoalRepositoryConfig;
import com.growit.fake.retrospect.FakeRetrospectRepository;
import com.growit.fake.retrospect.FakeRetrospectRepositoryConfig;
import com.growit.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.goal.domain.goal.Goal;
import com.growit.goal.domain.goal.GoalRepository;
import com.growit.goal.domain.goal.plan.Plan;
import com.growit.retrospect.domain.RetrospectRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@Import({FakeGoalRepositoryConfig.class, FakeRetrospectRepositoryConfig.class})
class RetrospectControllerTest {
  private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;
  @Autowired private GoalRepository goalRepository;
  @Autowired private RetrospectRepository retrospectRepository;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    TestSecurityUtil.setMockUser();
    if (goalRepository instanceof FakeGoalRepository fake) {
      fake.clear();
    }
    if (retrospectRepository instanceof FakeRetrospectRepository fake) {
      fake.clear();
    }
  }

  @Test
  void createRetrospect() throws Exception {
    // given
    Plan plan = Plan.builder().id("plan-id").weekOfMonth(1).content("주간 계획").build();

    Goal goal =
        Goal.builder().id("goal-id").userId("user-id").name("목표").plans(List.of(plan)).build();

    goalRepository.saveGoal(goal);

    CreateRetrospectRequest request =
        new CreateRetrospectRequest(
            "goal-id", "plan-id", "이번 주 회고입니다. 계획한 대로 잘 진행되었으며, 다음 주에도 열심히 해보겠습니다.");

    // when & then
    mockMvc
        .perform(
            post("/retrospects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").exists())
        .andDo(
            document(
                "create-retrospect",
                requestFields(
                    fieldWithPath("goalId").type(JsonFieldType.STRING).description("목표 ID"),
                    fieldWithPath("planId").type(JsonFieldType.STRING).description("계획 ID"),
                    fieldWithPath("content")
                        .type(JsonFieldType.STRING)
                        .description("회고 내용 (10~200자)")),
                responseFields(
                    fieldWithPath("data.id").type(JsonFieldType.STRING).description("생성된 회고 ID"))));
  }
}
