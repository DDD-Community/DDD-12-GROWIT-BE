package com.growit.app.goal.usecase;

import com.growit.app.goal.domain.retrospect.Retrospect;
import com.growit.app.goal.domain.retrospect.RetrospectRepository;
import com.growit.app.goal.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.goal.domain.retrospect.service.RetrospectValidator;
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
    retrospectValidator.validateCreateRetrospect(command);

    Retrospect retrospect = Retrospect.from(command);
    retrospectRepository.saveRetrospect(retrospect);

    return retrospect.getId();
  }
}