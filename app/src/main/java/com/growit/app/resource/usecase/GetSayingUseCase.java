package com.growit.app.resource.usecase;

import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetSayingUseCase {
  private final SayingRepository sayingRepository;
  private final Saying defaultSaying = new Saying("tempId", "성공은 매일 반복되는 작은 노력들의 합이다냥!", "그로냥");

  public Saying execute() {
    final List<Saying> sayings = sayingRepository.findAll();

    return sayings.isEmpty()
        ? defaultSaying
        : sayings.get(LocalDateTime.now().getMinute() % sayings.size());
  }
}
