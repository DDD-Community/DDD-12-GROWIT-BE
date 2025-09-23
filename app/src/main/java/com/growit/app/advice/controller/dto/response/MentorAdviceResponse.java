package com.growit.app.advice.controller.dto.response;

public record MentorAdviceResponse(
    boolean isChecked,
    String message,
    com.growit.app.advice.controller.dto.response.MentorAdviceResponse.KptResponse kpt) {
  public record KptResponse(String keep, String problem, String tryNext) {}
}
