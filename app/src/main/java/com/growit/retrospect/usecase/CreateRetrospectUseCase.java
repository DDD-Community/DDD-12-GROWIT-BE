package com.growit.retrospect.usecase;

import com.growit.retrospect.domain.Retrospect;
import com.growit.retrospect.domain.RetrospectRepository;
import com.growit.retrospect.domain.dto.CreateRetrospectCommand;
import com.growit.retrospect.domain.service.RetrospectValidator;
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
