package com.growit.app.goal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGoalRequest {
  @NotBlank(message = "목표명은 필수입니다.")
  @Size(max = 128, message = "목표명은 128자 이하여야 합니다.")
  private String name;

  @NotNull(message = "시작날짜는 필수입니다.")
  private LocalDate startDate;

  @NotNull(message = "종료날짜는 필수입니다.")
  private LocalDate endDate;

  @NotBlank(message = "현재 상태는 필수입니다.")
  @Size(max = 128, message = "현재 상태는 128자 이하여야 합니다.")
  private String asIs;

  @NotBlank(message = "목표 상태는 필수입니다.")
  @Size(max = 128, message = "목표 상태는 128자 이하여야 합니다.")
  private String toBe;

  @NotNull(message = "계획 목록은 필수입니다.")
  @Valid
  private List<PlanRequest> plans;

  @Getter
  @Setter
  public static class PlanRequest {
    @NotBlank(message = "주간 계획은 필수입니다.")
    @Size(max = 20, message = "주간 계획은 20자 이하여야 합니다.")
    private String content;
  }
}