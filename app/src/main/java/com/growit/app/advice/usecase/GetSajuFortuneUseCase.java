package com.growit.app.advice.usecase;

import com.growit.app.advice.domain.saju.service.SajuFortuneClient;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSajuFortuneUseCase {

  private final SajuFortuneClient sajuFortuneClient;

  public Map<String, Object> execute(String birthDate, String birthTime, String gender) {
    return sajuFortuneClient.getFortune(birthDate, birthTime, gender);
  }
}
