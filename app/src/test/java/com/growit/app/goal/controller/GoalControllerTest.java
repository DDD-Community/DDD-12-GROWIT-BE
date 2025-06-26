package com.growit.app.goal.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.common.FakeGoalRepositoryConfig;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.goal.domain.goal.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@Import(FakeGoalRepositoryConfig.class)
class GoalControllerTest {
  private MockMvc mockMvc;

  //  @MockitoBean
//  private GetUserGoalsUseCase getUserGoalsUseCase;
//
//  @MockitoBean
//  private DeleteGoalUseCase deleteGoalUseCase;
  private FakeGoalRepository fakeGoalRepository;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    fakeGoalRepository = new FakeGoalRepository();
    this.mockMvc =
      MockMvcBuilders.webAppContextSetup(context)
        .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
        .build();
    TestSecurityUtil.setMockUser();
  }

  @Test
  void getMyGoal() throws Exception {
    // given
    Goal goal = GoalFixture.defaultGoal();
    fakeGoalRepository.saveGoal(goal);

    // when & then
    mockMvc.perform(get("/goals").header("Authorization", "Bearer mock-jwt-token"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data").exists())
      .andDo(
        document(
          "get-my-goal",
          new ResourceSnippetParametersBuilder()
            .tag("Goals")
            .description("내 목표 조회")
            .responseFields(
              fieldWithPath("data[].id").type(STRING).description("목표 ID"),
              fieldWithPath("data[].name").type(STRING).description("목표 이름"),
              fieldWithPath("data[].duration").description("기간 정보 객체"),
              fieldWithPath("data[].duration.startDate")
                .type(STRING)
                .description("시작일 (yyyy-MM-dd)"),
              fieldWithPath("data[].duration.endDate")
                .type(STRING)
                .description("종료일 (yyyy-MM-dd)"),
              fieldWithPath("data[].beforeAfter").description("전/후 상태 정보 객체"),
              fieldWithPath("data[].beforeAfter.asIs")
                .type(STRING)
                .description("현재 상태(As-Is)"),
              fieldWithPath("data[].beforeAfter.toBe")
                .type(STRING)
                .description("목표 달성 후 상태(To-Be)"),
              fieldWithPath("data[].plans").description("계획 리스트"),
              fieldWithPath("data[].plans[].id").type(STRING).description("계획 ID"),
              fieldWithPath("data[].plans[].content")
                .type(STRING)
                .description("계획 내용"))));
  }

  @Test
  void createGoal() throws Exception {
    String requestBody =
      """
        {
            "name": "내 목표는 그로잇 완성",
            "duration": {
              "startDate": "2025-06-23",
              "endDate": "2025-07-20"
            },
            "beforeAfter": {
                "asIs": "기획 정의",
                "toBe": "배포 완료"
            },
            "plans": [
                {"content" : "기획 및 설계 회의"},
                {"content" : "디자인 시안 뽑기"},
                {"content" : "프론트 개발 및 백 개발 완료"},
                {"content" : "배포 완료"}
            ]
        }
        """;
    mockMvc
      .perform(
        post("/goals")
          .header("Authorization", "Bearer mock-jwt-token")
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody))
      .andExpect(status().isCreated())
      .andDo(
        document(
          "create-goal",
          new ResourceSnippetParametersBuilder()
            .tag("Goals")
            .description("목표 생성")
            .requestFields(
              fieldWithPath("name").type(JsonFieldType.STRING).description("목표 이름"),
              fieldWithPath("duration.startDate")
                .type(JsonFieldType.STRING)
                .description("시작일 (yyyy-MM-dd)"),
              fieldWithPath("duration.endDate")
                .type(JsonFieldType.STRING)
                .description("종료일 (yyyy-MM-dd)"),
              fieldWithPath("beforeAfter.asIs")
                .type(JsonFieldType.STRING)
                .description("현재 상태(As-Is)"),
              fieldWithPath("beforeAfter.toBe")
                .type(JsonFieldType.STRING)
                .description("목표 달성 후 상태(To-Be)"),
              fieldWithPath("plans[].content")
                .type(JsonFieldType.STRING)
                .description("계획 내용"))
            .responseFields(fieldWithPath("data.id").type(STRING).description("목표 ID"))));
  }

//  @Test
//  void deleteGoal_success() throws Exception {
//    Goal goal = GoalFixture.defaultGoal();
//    fakeGoalRepository.saveGoal(goal);
//    System.out.println("ID :: " + goal.getId());
//    mockMvc.perform(delete("/goals/{id}", goal.getId()).header("Authorization", "Bearer mock-jwt-token"))
//      .andExpect(status().isOk())
//      .andExpect(jsonPath("$.message").value("입력한 정보가 올바르지 않습니다. \n이미 삭제된 데이터입니다."))
//      .andDo(document("delete-goal",
//        new ResourceSnippetParametersBuilder()
//          .tag("Goals")
//          .description("목표 삭제")
//          .pathParameters(
//            parameterWithName("id").description("삭제할 목표 ID")
//          )
//          .responseFields(
//            fieldWithPath("data").type(JsonFieldType.STRING).description("삭제 완료 메시지")
//          )));
//  }
}
