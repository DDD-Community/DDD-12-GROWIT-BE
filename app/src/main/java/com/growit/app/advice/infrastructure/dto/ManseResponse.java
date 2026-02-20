package com.growit.app.advice.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManseResponse {
  @JsonProperty("year")
  private String year; // 연주 (예: 갑진)

  @JsonProperty("month")
  private String month; // 월주 (예: 병인)

  @JsonProperty("day")
  private String day; // 일주 (예: 무자)

  @JsonProperty("time")
  private String time; // 시주 (예: 임술)
}
