package com.growit.app.user.infrastructure.persistence.user.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.user.vo.EarthlyBranchHour;
import com.growit.app.user.domain.user.vo.SajuInfo;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "saju_infos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SajuInfoEntity extends BaseEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender", nullable = false)
  private SajuInfo.Gender gender;

  @Column(name = "birth", nullable = false)
  private LocalDate birth;

  @Enumerated(EnumType.STRING)
  @Column(name = "birth_hour", nullable = false)
  private EarthlyBranchHour birthHour;

  @Column(name = "ganji_year")
  private String ganjiYear;

  @Column(name = "ganji_month")
  private String ganjiMonth;

  @Column(name = "ganji_day")
  private String ganjiDay;

  @Column(name = "ganji_hour")
  private String ganjiHour;

  public static SajuInfoEntity fromDomain(UserEntity user, SajuInfo sajuInfo) {
    if (sajuInfo == null) {
      return null;
    }

    return SajuInfoEntity.builder()
        .user(user)
        .gender(sajuInfo.gender())
        .birth(sajuInfo.birth())
        .birthHour(sajuInfo.birthHour())
        .ganjiYear(sajuInfo.ganjiYear())
        .ganjiMonth(sajuInfo.ganjiMonth())
        .ganjiDay(sajuInfo.ganjiDay())
        .ganjiHour(sajuInfo.ganjiHour())
        .build();
  }

  public SajuInfo toDomain() {
    if (this.gender == null || this.birth == null || this.birthHour == null) {
      return null;
    }

    return new SajuInfo(
        this.gender,
        this.birth,
        this.birthHour,
        this.ganjiYear,
        this.ganjiMonth,
        this.ganjiDay,
        this.ganjiHour);
  }

  public void updateByDomain(SajuInfo sajuInfo) {
    if (sajuInfo != null) {
      this.gender = sajuInfo.gender();
      this.birth = sajuInfo.birth();
      this.birthHour = sajuInfo.birthHour();
      this.ganjiYear = sajuInfo.ganjiYear();
      this.ganjiMonth = sajuInfo.ganjiMonth();
      this.ganjiDay = sajuInfo.ganjiDay();
      this.ganjiHour = sajuInfo.ganjiHour();
    }
  }

  public boolean hasAllFields() {
    return gender != null && birth != null && birthHour != null;
  }
}
