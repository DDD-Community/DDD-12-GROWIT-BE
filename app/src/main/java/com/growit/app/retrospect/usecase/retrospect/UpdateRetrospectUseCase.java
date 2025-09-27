package com.growit.app.retrospect.usecase.retrospect;

import com.growit.app.common.exception.BadRequestException;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.dto.UpdateRetrospectCommand;
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
    if (command.kpt() == null) {
      throw new BadRequestException("kpt is required");
    }
    Retrospect retrospect = retrospectQuery.getMyRetrospect(command.id(), command.userId());
    retrospect.updateBy(command);
    retrospectRepository.saveRetrospect(retrospect);
  }
}
