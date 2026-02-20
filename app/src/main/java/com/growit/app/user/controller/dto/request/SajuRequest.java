package com.growit.app.user.controller.dto.request;

import com.growit.app.user.domain.user.vo.EarthlyBranchHour;
import com.growit.app.user.domain.user.vo.SajuInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SajuRequest {

  @NotNull(message = "성별은 필수입니다")
  private SajuInfo.Gender gender;

  @NotNull(message = "생년월일은 필수입니다")
  @Past(message = "생년월일은 과거 날짜여야 합니다")
  private LocalDate birth;

  @NotNull(message = "태어난 시간은 필수입니다")
  private EarthlyBranchHour birthHour;
}
