package com.growit.app.advice.domain.exception;

public class AiServiceDisabledException extends RuntimeException {

  public AiServiceDisabledException() {
    super("AI 조언 서비스가 일시적으로 비활성화되었습니다.");
  }
}
