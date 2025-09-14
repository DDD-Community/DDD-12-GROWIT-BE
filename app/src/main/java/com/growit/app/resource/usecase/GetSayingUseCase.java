package com.growit.app.resource.usecase;

import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetSayingUseCase {
  private final SayingRepository sayingRepository;
  private static final Saying DEFAULT_SAYING =
      new Saying("tempId", "성공은 매일 반복되는 작은 노력들의 합이다냥!", "그로냥");

  // 요청마다 다른 격언을 보여주기 위한 랜덤 인스턴스
  private final Random random = new Random();

  public Saying execute() {
    final List<Saying> sayings = sayingRepository.findAll();

    if (sayings.isEmpty()) {
      return DEFAULT_SAYING;
    }

    // 요청마다 랜덤하게 격언 선택 (더 자주 변경됨)
    int randomIndex = random.nextInt(sayings.size());
    return sayings.get(randomIndex);
  }
}
