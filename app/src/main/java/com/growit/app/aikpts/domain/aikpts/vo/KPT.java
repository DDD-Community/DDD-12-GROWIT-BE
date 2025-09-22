package com.growit.app.aikpts.domain.aikpts.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class KPT {
  private final List<String> keeps;
  private final List<String> problems;
  private final List<String> trys;

  /**
   * KPT가 유효한지 검증
   */
  public boolean isValid() {
    return keeps != null && problems != null && trys != null;
  }

  /**
   * KPT가 비어있는지 확인
   */
  public boolean isEmpty() {
    return (keeps == null || keeps.isEmpty()) &&
           (problems == null || problems.isEmpty()) &&
           (trys == null || trys.isEmpty());
  }

  /**
   * KPT의 총 항목 수 반환
   */
  public int getTotalItemCount() {
    int count = 0;
    if (keeps != null) count += keeps.size();
    if (problems != null) count += problems.size();
    if (trys != null) count += trys.size();
    return count;
  }
}
