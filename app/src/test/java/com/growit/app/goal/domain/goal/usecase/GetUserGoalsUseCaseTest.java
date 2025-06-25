package com.growit.app.goal.domain.goal.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.GoalDto;
import com.growit.app.goal.usecase.GetUserGoalsUseCase;
import com.growit.app.user.domain.user.User;
import com.growit.app.utils.fixture.GoalTestBuilder;
import com.growit.app.utils.fixture.UserTestBuilder;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GetUserGoalsUseCaseTest {

  @Mock private GoalRepository goalRepository;
  @InjectMocks private GetUserGoalsUseCase getUserGoalsUseCase;

  private User testUser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    testUser = UserTestBuilder.aUser().withId("user-1").build();
  }

  @Test
  void givenUserWithGoals_whenGetMyGoals_thenReturnGoalResponseList() {
    // given
    Goal fakeGoal = GoalTestBuilder.aGoal().build();
    given(goalRepository.findByUserId("user-1")).willReturn(List.of(fakeGoal));

    GoalDto fakeGoalDto = GoalDto.from(fakeGoal);
    // when
    List<GoalDto> result = getUserGoalsUseCase.getMyGoals(testUser);

    // then
    assertThat(result).containsExactly(fakeGoalDto);
  }

  @Test
  void givenUserWithoutGoals_whenGetMyGoals_thenReturnEmptyList() {
    // given
    given(goalRepository.findByUserId("user-1")).willReturn(Collections.emptyList());

    // when
    List<GoalDto> result = getUserGoalsUseCase.getMyGoals(testUser);

    // then
    assertThat(result).isEmpty();
  }
}
