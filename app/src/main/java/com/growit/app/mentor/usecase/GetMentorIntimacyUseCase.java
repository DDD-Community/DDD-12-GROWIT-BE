package com.growit.app.mentor.usecase;

import com.growit.app.mentor.domain.MentorIntimacy;
import com.growit.app.mentor.domain.vo.IntimacyLevel;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.todo.domain.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMentorIntimacyUseCase {
  private final ToDoRepository toDoRepository;
  private final RetrospectRepository retrospectRepository;

  public IntimacyLevel execute(String userId) {
    int totalTodoCount = toDoRepository.countByUserId(userId);
    int weeklyRetrospectCount = retrospectRepository.countWeeklyRetrospectsByUserId(userId);

    return MentorIntimacy.calculateLevel(totalTodoCount, weeklyRetrospectCount);
  }
}
