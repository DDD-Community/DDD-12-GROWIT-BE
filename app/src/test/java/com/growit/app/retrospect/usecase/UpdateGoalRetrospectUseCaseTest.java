package com.growit.app.retrospect.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.retrospect.domain.goalretrospect.GoalRetrospectRepository;
import com.growit.app.retrospect.domain.goalretrospect.dto.UpdateGoalRetrospectCommand;
import com.growit.app.retrospect.domain.goalretrospect.service.GoalRetrospectQuery;
import com.growit.app.retrospect.usecase.goalretrospect.UpdateGoalRetrospectUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateGoalRetrospectUseCaseTest {

  @Mock private GoalRetrospectQuery goalRetrospectQuery;

  @Mock private GoalRetrospectRepository goalRetrospectRepository;

  @InjectMocks private UpdateGoalRetrospectUseCase useCase;

  @Test
  void givenInvalidCommand_whenExecute_thenReturnsNotFoundException() {
    // Given
    String id = "invalid-goal-retrospect-id";
    String userId = "user-id";
    String content = "수정된 목표 회고 내용";
    when(goalRetrospectQuery.getMyGoalRetrospect(id, userId)).thenThrow(NotFoundException.class);

    UpdateGoalRetrospectCommand command = new UpdateGoalRetrospectCommand(id, userId, content);

    // When & Then
    assertThrows(NotFoundException.class, () -> useCase.execute(command));
  }
}
