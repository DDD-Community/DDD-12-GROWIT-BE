package com.growit.app.aikpts.domain.aikpts;

import com.growit.app.aikpts.domain.aikpts.vo.KPT;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AIKPTS {
  private String id;
  private String aiAdviceId;
  private KPT kpt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Getter(AccessLevel.NONE)
  private boolean isDeleted;

  public void delete() {
    this.isDeleted = true;
  }

  /**
   * AIKPTS가 유효한지 검증
   */
  public boolean isValid() {
    return id != null && !id.trim().isEmpty() &&
           aiAdviceId != null && !aiAdviceId.trim().isEmpty() &&
           kpt != null && kpt.isValid();
  }

  /**
   * AIKPTS가 비어있는지 확인
   */
  public boolean isEmpty() {
    return kpt == null || kpt.isEmpty();
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  /**
   * AI 조언 응답으로부터 AIKPTS를 생성하는 팩토리 메서드
   */
  public static AIKPTS fromAdvice(String id, String aiAdviceId, KPT kpt) {
    return AIKPTS.builder()
        .id(id)
        .aiAdviceId(aiAdviceId)
        .kpt(kpt)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .isDeleted(false)
        .build();
  }

  /**
   * 기존 AIKPTS를 업데이트하는 메서드
   */
  public AIKPTS updateFromAdvice(KPT newKpt) {
    return AIKPTS.builder()
        .id(this.id)
        .aiAdviceId(this.aiAdviceId)
        .kpt(newKpt)
        .createdAt(this.createdAt)
        .updatedAt(LocalDateTime.now())
        .isDeleted(false)
        .build();
  }
}
