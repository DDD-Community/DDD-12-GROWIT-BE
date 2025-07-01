package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.goal.retrospect.Retrospect;
import com.growit.app.goal.domain.goal.retrospect.RetrospectRepository;
import com.growit.app.goal.domain.goal.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.goal.domain.goal.retrospect.service.RetrospectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRetrospectUseCase {
  private final RetrospectValidator retrospectValidator;
  private final RetrospectRepository retrospectRepository;

  @Transactional
  public String execute(CreateRetrospectCommand command) {
    // Validate content length
    retrospectValidator.validateContent(command.content());
    
    // Validate that the plan exists for the given goal
    retrospectValidator.validatePlanExists(command.goalId(), command.planId());
    
    // Validate that there's no existing retrospect for this goal/plan combination
    retrospectValidator.validateUniqueRetrospect(command.goalId(), command.planId());
    
    // Create and save the retrospect
    Retrospect retrospect = Retrospect.from(command);
    retrospectRepository.saveRetrospect(retrospect);
    
    return retrospect.getId();
  }
}