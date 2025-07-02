package com.growit.app.retrospect.usecase;

import com.growit.app.goal.domain.goal.service.GoalValidator;
import com.growit.app.retrospect.domain.retrospect.Retrospect;
import com.growit.app.retrospect.domain.retrospect.RetrospectRepository;
import com.growit.app.retrospect.domain.retrospect.dto.CreateRetrospectCommand;
import com.growit.app.retrospect.domain.retrospect.service.RetrospectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRetrospectUseCase {
  private final GoalValidator goalValidator;
  private final RetrospectValidator retrospectValidator;
  private final RetrospectRepository retrospectRepository;

  @Transactional
  public String execute(CreateRetrospectCommand command) {
    // 회고 내용 유효성 검증
    retrospectValidator.checkContent(command.content());

    // 계획 존재 여부 확인
    goalValidator.checkPlanExists(command.userId(), command.goalId(), command.planId());

    // 이미 회고가 존재하는지 확인
    retrospectValidator.checkUniqueRetrospect(command.planId());

    // 회고 생성 및 저장
    Retrospect retrospect = Retrospect.from(command);
    retrospectRepository.saveRetrospect(retrospect);

    return retrospect.getId();
  }
}
