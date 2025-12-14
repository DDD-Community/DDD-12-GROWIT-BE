package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.dto.UpdatePlanCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdatePlanUseCase {
  // Plan functionality has been removed from the system

  @Transactional
  public void execute(UpdatePlanCommand command) {
    // Plan domain has been deleted - this operation is no longer supported
    throw new UnsupportedOperationException("Plan functionality has been removed from the system");
  }
}
