package com.growit.app.retrospect.usecase;

import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.command.UpdateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateRetrospectUseCase {
  private final RetrospectQuery retrospectQuery;
  private final RetrospectRepository retrospectRepository;

  @Transactional
  public void execute(UpdateRetrospectCommand command) {
    Retrospect retrospect = retrospectQuery.getMyRetrospect(command.id(), command.userId());
    retrospect.updateBy(command);
    retrospectRepository.saveRetrospect(retrospect);
  }
}
