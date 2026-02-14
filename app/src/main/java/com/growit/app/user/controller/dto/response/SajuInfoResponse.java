package com.growit.app.user.controller.dto.response;

import com.growit.app.user.domain.user.vo.EarthlyBranchHour;
import com.growit.app.user.domain.user.vo.SajuInfo;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SajuInfoResponse {

  private SajuInfo.Gender gender;
  private LocalDate birth;
  private EarthlyBranchHour birthHour;

  public static SajuInfoResponse from(SajuInfo sajuInfo) {
    return SajuInfoResponse.builder()
        .gender(sajuInfo.gender())
        .birth(sajuInfo.birth())
        .birthHour(sajuInfo.birthHour())
        .build();
  }
}
