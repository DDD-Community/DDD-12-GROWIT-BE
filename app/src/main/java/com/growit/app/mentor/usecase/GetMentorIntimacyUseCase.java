package com.growit.app.mentor.usecase;

import com.growit.app.mentor.domain.MentorIntimacy;
import com.growit.app.mentor.domain.service.MentorIntimacyService;
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
  private final MentorIntimacyService mentorIntimacyService;
  private final ToDoRepository toDoRepository;
  private final RetrospectRepository retrospectRepository;

  public IntimacyLevel execute(String userId) {
    mentorIntimacyService.validateUserId(userId);
    System.out.println(userId);
    int totalTodoCount = toDoRepository.countByUserId(userId);
    System.out.println(totalTodoCount);
    int weeklyRetrospectCount = retrospectRepository.countWeeklyRetrospectsByUserId(userId);
    System.out.println(weeklyRetrospectCount);

    return MentorIntimacy.calculateLevel(totalTodoCount, weeklyRetrospectCount);
  }
}
