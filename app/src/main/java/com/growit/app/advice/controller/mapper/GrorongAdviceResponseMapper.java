package com.growit.app.advice.controller.mapper;

import com.growit.app.advice.controller.dto.response.GrorongAdviceResponse;
import com.growit.app.advice.domain.grorong.Grorong;
import org.springframework.stereotype.Component;

@Component
public class GrorongAdviceResponseMapper {

  public GrorongAdviceResponse toResponse(Grorong grorong) {
    return new GrorongAdviceResponse(grorong);
  }
}
