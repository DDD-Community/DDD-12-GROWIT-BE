package com.growit.app.resource.controller.dto.request;

import com.growit.app.resource.domain.saying.Saying;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SyncSayingsRequest {
  @NotEmpty(message = "sayings는 비어 있을 수 없습니다.")
  private List<Saying> sayings;
}
