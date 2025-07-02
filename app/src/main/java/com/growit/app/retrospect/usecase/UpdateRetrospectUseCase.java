package com.growit.app.retrospect.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.Goal;
import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.command.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.command.UpdateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateRetrospectUseCase {
  private final RetrospectValidator retrospectValidator;
  private final RetrospectRepository retrospectRepository;

  @Transactional
  public void execute(UpdateRetrospectCommand command) {
    Retrospect retrospect = retrospectRepository.findById(command.id())
      .orElseThrow(() -> new NotFoundException("회고 정보가 존재하지 않습니다."));

    retrospectValidator.checkMyRetrospect(retrospect, command.userId());

    retrospect.updateBy(command);
    retrospectRepository.saveRetrospect(retrospect);
  }
}
