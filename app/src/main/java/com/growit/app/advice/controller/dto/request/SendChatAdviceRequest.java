package com.growit.app.advice.controller.dto.request;

import com.growit.app.advice.domain.chatadvice.vo.AdviceStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendChatAdviceRequest {
  @NotNull(message = "주차는 필수입니다.")
  private Integer week;

  @NotBlank(message = "사용자 메시지는 필수입니다.")
  @Size(min = 5, max = 100, message = "사용자 메시지는 5자 이상 100자 이하여야 합니다.")
  private String userMessage;

  @NotBlank(message = "목표 ID는 필수입니다.")
  private String goalId;

  @NotNull(message = "조언 스타일은 필수입니다.")
  private AdviceStyle adviceStyle;

  @com.fasterxml.jackson.annotation.JsonProperty("isOnboarding")
  private Boolean isOnboarding;
}
