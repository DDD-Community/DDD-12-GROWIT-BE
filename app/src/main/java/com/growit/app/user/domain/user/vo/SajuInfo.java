package com.growit.app.user.domain.user.vo;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public record SajuInfo(Gender gender, LocalDate birth, EarthlyBranchHour birthHour) {

  public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String label;

    Gender(String label) {
      this.label = label;
    }

    public String getLabel() {
      return label;
    }
  }
}
