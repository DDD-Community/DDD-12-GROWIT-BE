package com.growit.app.user.domain.user.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CareerYear {
  NEWBIE("신입 1년차 미만"), JUNIOR("주니어(1~3년)"), MID("미드레벨(3~6년)"), SENIOR("시니어(6~10년)"), LEAD("리드/매니저(10년 이상)");
  private final String label;

}
