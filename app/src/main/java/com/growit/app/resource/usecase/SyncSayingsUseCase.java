package com.growit.app.resource.usecase;

import com.growit.app.resource.domain.saying.command.SyncSayingsCommand;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SyncSayingsUseCase {
  private final SayingRepository sayingRepository;

  @Transactional
  public void execute(SyncSayingsCommand command) {
    command.sayings().forEach(sayingRepository::save);
  }
}
