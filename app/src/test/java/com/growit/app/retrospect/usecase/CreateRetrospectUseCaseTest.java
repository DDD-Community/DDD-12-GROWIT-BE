package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.growit.app.fake.goal.FakeGoalRepository;
import com.growit.app.fake.goal.GoalFixture;
import com.growit.app.fake.retrospect.FakeRetrospectRepository;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalService;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectService;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateRetrospectUseCaseTest {

  private CreateRetrospectUseCase createRetrospectUseCase;
  Goal goal = GoalFixture.defaultGoal();

  @BeforeEach
  void setUp() {
    FakeGoalRepository goalRepository = new FakeGoalRepository();
    goalRepository.saveGoal(goal);

    GoalValidator goalValidator = new GoalService(goalRepository);
    FakeRetrospectRepository retrospectRepository = new FakeRetrospectRepository();
    RetrospectValidator retrospectValidator = new RetrospectService(retrospectRepository);
    createRetrospectUseCase =
        new CreateRetrospectUseCase(goalValidator, retrospectValidator, retrospectRepository);
  }

  @Test
  void givenValidCommand_whenExecute_thenReturnsRetrospectId() {
    // Given
    CreateRetrospectCommand command =
        RetrospectFixture.createRetrospectCommandWithContent(
            goal.getId(),
            goal.getUserId(),
            goal.getPlans().get(0).getId(),
            "이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.");

    // When
    String retrospectId = createRetrospectUseCase.execute(command);

    // Then
    assertNotNull(retrospectId);
  }
}
