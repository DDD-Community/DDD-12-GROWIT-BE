package com.growit.app.advice.usecase.dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChatAdviceStatusDto {
  private boolean isOnboardingCompleted;
  private List<Map<String, Object>> messages;
  private Map<String, Object> weekRecord;
}
