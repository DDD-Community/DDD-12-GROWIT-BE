package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.exception.AiServiceDisabledException;
import com.growit.app.advice.domain.saju.service.SajuFortuneClient;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SajuFortuneClientImpl implements SajuFortuneClient {

  @Override
  public Map<String, Object> getFortune(String birthDate, String birthTime, String gender) {
    throw new AiServiceDisabledException();
  }

  @Override
  public Map<String, String> getManse(String birthDate, String birthTime, String gender) {
    return Map.of();
  }
}
