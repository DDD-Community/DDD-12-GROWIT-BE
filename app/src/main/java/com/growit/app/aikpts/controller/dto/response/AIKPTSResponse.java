package com.growit.app.aikpts.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AIKPTSResponse {
  private String id;
  private String aiAdviceId;
  private List<String> keeps;
  private List<String> problems;
  private List<String> trys;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
