package com.growit.app.advice.domain.saju.service;

import java.util.Map;

public interface SajuFortuneClient {

  Map<String, Object> getFortune(String birthDate, String birthTime, String gender);

  Map<String, String> getManse(String birthDate, String birthTime, String gender);
}
