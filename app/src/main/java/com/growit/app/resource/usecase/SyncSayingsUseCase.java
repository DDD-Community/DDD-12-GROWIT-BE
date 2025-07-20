package com.growit.app.resource.usecase;

import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.domain.saying.command.SyncSayingsCommand;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SyncSayingsUseCase {
  private final SayingRepository sayingRepository;

  @Transactional
  public void execute(SyncSayingsCommand command) {
    List<Saying> sayings =
        command.getSayings().stream()
            .map(
                data ->
                    Saying.builder()
                        .id(data.getId())
                        .message(data.getMessage())
                        .author(data.getAuthor())
                        .build())
            .toList();

    sayingRepository.syncAll(sayings);
  }
}
