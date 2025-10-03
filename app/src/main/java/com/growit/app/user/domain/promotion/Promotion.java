package com.growit.app.user.domain.promotion;

import com.growit.app.common.util.IDGenerator;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Promotion {
  private String id;
  private String code;
  private PromotionType type;
  private LocalDateTime expiredAt;
  private boolean isActive;
  private boolean isUsed;

  public static Promotion create(String code, PromotionType type, LocalDateTime expiredAt) {
    return Promotion.builder()
        .id(IDGenerator.generateId())
        .code(code)
        .type(type)
        .expiredAt(expiredAt)
        .isActive(true)
        .isUsed(false)
        .build();
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiredAt);
  }

  public boolean isValid() {
    return isActive && !isExpired();
  }

  public boolean canBeUsed() {
    return !isUsed && isValid();
  }

  public void deactivate() {
    this.isActive = false;
  }

  public void markAsUsed() {
    this.isUsed = true;
  }
}
