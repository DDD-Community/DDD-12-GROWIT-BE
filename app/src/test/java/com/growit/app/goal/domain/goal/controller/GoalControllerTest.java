package com.growit.app.goal.domain.goal.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.goal.controller.mapper.GoalResponseMapper;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.dto.GoalDto;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.utils.fixture.GoalTestBuilder;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
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
public class GoalControllerTest {
  private MockMvc mockMvc;

  @MockitoBean private GetUserGoalsUseCase getUserGoalsUseCase;
  private GoalResponseMapper responseMapper;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
  }

  @Test
  void getMyGoal_success() throws Exception {
    // given
    Goal goal = GoalTestBuilder.aGoal().build();
    GoalDto fakeDto = GoalDto.from(goal);
    List<GoalDto> fakeGoalList = List.of(fakeDto);

    given(getUserGoalsUseCase.getMyGoals(any())).willReturn(fakeGoalList);

    // when & then
    mockMvc
        .perform(get("/goals"))
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
}
