package com.growit.app.goal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRetrospectRequest(
    @NotBlank(message = "목표 ID는 필수입니다.")
    String goalId,
    
    @NotBlank(message = "계획 ID는 필수입니다.")
    String planId,
    
    @NotBlank(message = "회고 내용은 필수입니다.")
    @Size(min = 10, max = 200, message = "회고 내용은 10자 이상 200자 이하로 작성해주세요.")
    String content
) {}