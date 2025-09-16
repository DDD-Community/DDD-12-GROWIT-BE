package com.growit.app.mentor.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.growit.app.fake.mentor.MentorFixture;
import com.growit.app.mentor.domain.vo.IntimacyLevel;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.todo.domain.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMentorIntimacyUseCaseTest {

  private GetMentorIntimacyUseCase getMentorIntimacyUseCase;

  @Mock private ToDoRepository toDoRepository;
  @Mock private RetrospectRepository retrospectRepository;

  @BeforeEach
  void setUp() {
    getMentorIntimacyUseCase = new GetMentorIntimacyUseCase(toDoRepository, retrospectRepository);
  }

  @Test
  void givenValidUserId_whenExecute_thenReturnsHighIntimacyLevel() {
    // Given
    String userId = MentorFixture.defaultUserId();
    when(toDoRepository.countByUserId(userId)).thenReturn(60);
    when(retrospectRepository.countWeeklyRetrospectsByUserId(userId)).thenReturn(3);

    // When
    IntimacyLevel result = getMentorIntimacyUseCase.execute(userId);

    // Then
    assertEquals(IntimacyLevel.HIGH, result);
  }

  @Test
  void givenValidUserId_whenExecute_thenReturnsMediumIntimacyLevel() {
    // Given
    String userId = MentorFixture.defaultUserId();
    when(toDoRepository.countByUserId(userId)).thenReturn(30);
    when(retrospectRepository.countWeeklyRetrospectsByUserId(userId)).thenReturn(2);

    // When
    IntimacyLevel result = getMentorIntimacyUseCase.execute(userId);

    // Then
    assertEquals(IntimacyLevel.MEDIUM, result);
  }

  @Test
  void givenValidUserId_whenExecute_thenReturnsLowIntimacyLevel() {
    // Given
    String userId = MentorFixture.defaultUserId();
    when(toDoRepository.countByUserId(userId)).thenReturn(15);
    when(retrospectRepository.countWeeklyRetrospectsByUserId(userId)).thenReturn(1);

    // When
    IntimacyLevel result = getMentorIntimacyUseCase.execute(userId);

    // Then
    assertEquals(IntimacyLevel.LOW, result);
  }

}
