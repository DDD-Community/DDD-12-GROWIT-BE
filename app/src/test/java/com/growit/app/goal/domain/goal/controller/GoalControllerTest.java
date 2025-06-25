package com.growit.app.goal.domain.goal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.growit.app.goal.controller.dto.GoalResponse;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
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
    GoalResponse fakeResponse = new GoalResponse(/* ... */ );
    List<GoalResponse> fakeGoalList = List.of(fakeResponse);

    given(getUserGoalsUseCase.getMyGoals(any())).willReturn(fakeGoalList);

    // when & then
    mockMvc
        .perform(get("/goals"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists());
  }
}
