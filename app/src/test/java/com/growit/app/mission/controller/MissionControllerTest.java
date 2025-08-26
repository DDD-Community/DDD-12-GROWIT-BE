package com.growit.app.mission.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.mission.MissionFixture;
import com.growit.app.mission.controller.dto.CreateMissionRequest;
import com.growit.app.mission.controller.dto.UpdateMissionRequest;
import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.usecase.CreateMissionUseCase;
import com.growit.app.mission.usecase.GetMissionUseCase;
import com.growit.app.mission.usecase.UpdateMissionUseCase;
import java.util.List;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
class MissionControllerTest {

  private MockMvc mockMvc;

  @MockitoBean private CreateMissionUseCase createMissionUseCase;
  @MockitoBean private GetMissionUseCase getMissionUseCase;
  @MockitoBean private UpdateMissionUseCase updateMissionUseCase;

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
  void getMission() throws Exception {
    // given
    Mission mission = MissionFixture.defaultMission();
    given(getMissionUseCase.execute("user-1")).willReturn(List.of(mission));

    // when & then
    mockMvc
        .perform(get("/mission").header("Authorization", "Bearer mock-jwt-token"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "get-mission",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Mission")
                        .summary("오늘자 미션 조회")
                        .responseFields(subsectionWithPath("data").description("미션 목록 배열"))
                        .build())));
  }

  @Test
  void createMission() throws Exception {
    // given
    CreateMissionRequest body = new CreateMissionRequest(false, "drink water");

    mockMvc
        .perform(
            post("/mission")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "create-mission",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Mission")
                        .summary("미션 생성")
                        .requestFields(
                            fieldWithPath("finished")
                                .type(JsonFieldType.BOOLEAN)
                                .description("완료 여부"),
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("미션 내용"))
                        .responseFields(fieldWithPath("data").type(STRING).description("성공 메세지"))
                        .build())));
  }

  @Test
  void updateMission() throws Exception {
    // given
    UpdateMissionRequest body = new UpdateMissionRequest(true);

    mockMvc
        .perform(
            put("/mission/{id}", "mission-1")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andDo(
            document(
                "update-mission",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Mission")
                        .summary("미션 완료 처리")
                        .requestFields(
                            fieldWithPath("finished")
                                .type(JsonFieldType.BOOLEAN)
                                .description("완료 여부"))
                        .responseFields(fieldWithPath("data").type(STRING).description("성공 메세지"))
                        .build())));
    verify(updateMissionUseCase).execute(org.mockito.ArgumentMatchers.any());
  }
}
