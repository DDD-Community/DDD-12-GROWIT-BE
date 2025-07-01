package com.growit.app.goal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.growit.app.common.response.ApiResponse;
import com.growit.app.common.response.IdDto;
import com.growit.app.goal.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.goal.controller.mapper.RetrospectRequestMapper;
import com.growit.app.goal.domain.goal.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.goal.usecase.CreateRetrospectUseCase;
import com.growit.app.user.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class RetrospectControllerTest {

  @Mock private CreateRetrospectUseCase createRetrospectUseCase;
  @Mock private RetrospectRequestMapper retrospectRequestMapper;

  private RetrospectController retrospectController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    retrospectController = new RetrospectController(createRetrospectUseCase, retrospectRequestMapper);
  }

  @Test
  void givenValidRequest_whenCreateRetrospect_thenReturnCreated() {
    // Given
    User user = User.builder().id("user-123").build();
    CreateRetrospectRequest request = new CreateRetrospectRequest(
        "goal-123",
        "plan-456",
        "이번 주 계획을 잘 수행했습니다. 다음 주에는 더 많은 성과를 내도록 노력하겠습니다."
    );

    CreateRetrospectCommand command = new CreateRetrospectCommand(
        "goal-123",
        "plan-456",
        "user-123",
        "이번 주 계획을 잘 수행했습니다. 다음 주에는 더 많은 성과를 내도록 노력하겠습니다."
    );

    String retrospectId = "retrospect-789";
    
    when(retrospectRequestMapper.toCommand(eq("user-123"), eq(request))).thenReturn(command);
    when(createRetrospectUseCase.execute(command)).thenReturn(retrospectId);

    // When
    ResponseEntity<ApiResponse<IdDto>> response = retrospectController.createRetrospect(user, request);

    // Then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(retrospectId, response.getBody().data().id());
  }
}