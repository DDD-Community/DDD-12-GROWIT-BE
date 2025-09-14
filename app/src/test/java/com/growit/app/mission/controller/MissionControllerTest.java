package com.growit.app.mission.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.mission.MissionFixture;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.vo.MissionType;
import com.growit.app.mission.usecase.GetMissionUseCase;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
class MissionControllerTest {

  private MockMvc mockMvc;

  @MockitoBean private GetMissionUseCase getMissionUseCase;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
    TestSecurityUtil.setMockUser();
  }

  @Test
  void getMission() throws Exception {
    // given
    Mission mission1 = MissionFixture.missionWithType(MissionType.DAILY_TODO_WRITE);
    Mission mission2 = MissionFixture.missionWithType(MissionType.DAILY_TODO_COMPLETE);
    List<Mission> missions = List.of(mission1, mission2);

    given(getMissionUseCase.execute("user-1")).willReturn(missions);

    // when & then
    mockMvc
        .perform(get("/mission").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].type").value("DAILY_TODO_WRITE"))
        .andExpect(jsonPath("$.data[1].type").value("DAILY_TODO_COMPLETE"))
        .andDo(
            document(
                "get-mission",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Mission")
                        .summary("오늘자 미션 조회")
                        .description("현재 날짜의 요일에 해당하는 미션 목록을 조회합니다.")
                        .responseFields(
                            subsectionWithPath("data").description("미션 목록 배열"),
                            subsectionWithPath("data[].id").description("미션 ID"),
                            subsectionWithPath("data[].dayOfWeek").description("요일"),
                            subsectionWithPath("data[].content").description("미션 내용"),
                            subsectionWithPath("data[].type").description("미션 타입"),
                            subsectionWithPath("data[].finished").description("완료 여부"))
                        .build())));
  }

  @Test
  void getMission_with_empty_result() throws Exception {
    // given
    given(getMissionUseCase.execute("user-1")).willReturn(List.of());

    // when & then
    mockMvc
        .perform(get("/mission").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  void getMission_with_different_mission_types() throws Exception {
    // given
    Mission dailyTodoWrite = MissionFixture.missionWithType(MissionType.DAILY_TODO_WRITE);
    Mission dailyTodoComplete = MissionFixture.missionWithType(MissionType.DAILY_TODO_COMPLETE);
    Mission weeklyGoalWrite = MissionFixture.missionWithType(MissionType.WEEKLY_GOAL_WRITE);
    Mission weeklyRetrospectWrite =
        MissionFixture.missionWithType(MissionType.WEEKLY_RETROSPECT_WRITE);

    List<Mission> missions =
        List.of(dailyTodoWrite, dailyTodoComplete, weeklyGoalWrite, weeklyRetrospectWrite);
    given(getMissionUseCase.execute("user-1")).willReturn(missions);

    // when & then
    mockMvc
        .perform(get("/mission").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data").value(org.hamcrest.Matchers.hasSize(4)))
        .andExpect(jsonPath("$.data[0].type").value("DAILY_TODO_WRITE"))
        .andExpect(jsonPath("$.data[1].type").value("DAILY_TODO_COMPLETE"))
        .andExpect(jsonPath("$.data[2].type").value("WEEKLY_GOAL_WRITE"))
        .andExpect(jsonPath("$.data[3].type").value("WEEKLY_RETROSPECT_WRITE"));
  }
}
