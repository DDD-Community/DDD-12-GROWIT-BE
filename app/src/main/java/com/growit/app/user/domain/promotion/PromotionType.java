package com.growit.app.user.domain.promotion;

public enum PromotionType {
  AD_REMOVAL_ONE_YEAR("광고 제거 1년"),
  AD_REMOVAL_AI_ONE_YEAR("광고 제거+AI 구독 1년"),
  AD_REMOVAL_AI_ONE_MONTH("광고 제거+AI 구독 한달");

  private final String description;

  PromotionType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
