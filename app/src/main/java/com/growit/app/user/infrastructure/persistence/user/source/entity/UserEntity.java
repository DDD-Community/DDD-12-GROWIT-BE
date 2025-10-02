package com.growit.app.user.infrastructure.persistence.user.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.CareerYear;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false)
  private String email;

  @Column(nullable = true)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(name = "job_role_id", nullable = false)
  private String jobRoleId;

  @Enumerated(EnumType.STRING)
  @Column(name = "career_year", nullable = false)
  private CareerYear careerYear;

  @Column(nullable = false)
  private Boolean isOnboarding;

  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER // 이렇게 추가
      )
  private Set<OAuthAccountEntity> oauthAccounts;

  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private Set<UserPromotionMapEntity> userPromotions;

  public void updateByDomain(User user) {
    this.name = user.getName();
    this.jobRoleId = user.getJobRoleId();
    this.careerYear = user.getCareerYear();
    this.isOnboarding = user.isOnboarding();

    Set<String> existingKeys =
        this.oauthAccounts.stream()
            .map(e -> e.getProvider() + e.getProviderId())
            .collect(Collectors.toSet());

    // 도메인에서 넘어온 계정 중 아직 없는 것만 추가
    user.getOauthAccounts()
        .forEach(
            o -> {
              String key = o.provider() + o.providerId();
              if (!existingKeys.contains(key)) {
                OAuthAccountEntity newEntity =
                    OAuthAccountEntity.builder()
                        .user(this)
                        .provider(o.provider())
                        .providerId(o.providerId())
                        .build();

                this.oauthAccounts.add(newEntity);
              }
            });

    if (user.isDeleted()) {
      this.oauthAccounts.clear();
      this.setDeletedAt(LocalDateTime.now());
    }
  }
}
