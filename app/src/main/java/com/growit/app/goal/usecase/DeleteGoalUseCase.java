package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.goal.domain.goal.dto.DeleteGoalCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteGoalUseCase {
  private final GoalRepository goalRepository;

  @Transactional
  public String execute(DeleteGoalCommand command) {
    String uid = command.uid();
    String userId = command.userId();

    return "";
  }
}
