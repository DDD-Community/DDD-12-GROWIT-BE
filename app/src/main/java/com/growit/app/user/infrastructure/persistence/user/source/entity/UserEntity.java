package com.growit.app.user.infrastructure.persistence.user.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

  @Column(nullable = false)
  private Boolean isOnboarding;

  @Deprecated
  @Column(nullable = true)
  private String jobRoleId;

  @Deprecated
  @Column(nullable = true)
  private String careerYear;

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

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private SajuInfoEntity sajuInfo;

  public void updateByDomain(User user) {
    this.name = user.getName();
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
                        .refreshToken(o.refreshToken())
                        .build();

                this.oauthAccounts.add(newEntity);
              } else {
                this.oauthAccounts.stream()
                    .filter(
                        e ->
                            e.getProvider().equals(o.provider())
                                && e.getProviderId().equals(o.providerId()))
                    .findFirst()
                    .ifPresent(
                        e -> {
                          if (o.refreshToken() != null) {
                            e.updateRefreshToken(o.refreshToken());
                          }
                        });
              }
            });

    // 사주정보 업데이트
    if (user.getSaju() != null) {
      if (this.sajuInfo == null) {
        this.sajuInfo = SajuInfoEntity.fromDomain(this, user.getSaju());
      } else {
        this.sajuInfo.updateByDomain(user.getSaju());
      }
    }

    if (user.isDeleted()) {
      this.oauthAccounts.clear();
      this.setDeletedAt(LocalDateTime.now());
    }
  }

  public User toDomain() {
    ArrayList<OAuth> oauthList = new ArrayList<>();
    if (this.oauthAccounts != null) {
      this.oauthAccounts.forEach(
          o -> oauthList.add(new OAuth(o.getProvider(), o.getProviderId(), o.getRefreshToken())));
    }

    return User.builder()
        .id(this.uid)
        .email(new Email(this.email))
        .password(this.password)
        .name(this.name)
        .isOnboarding(this.isOnboarding)
        .isDeleted(getDeletedAt() != null)
        .oauthAccounts(oauthList)
        .saju(this.sajuInfo != null ? this.sajuInfo.toDomain() : null)
        .jobRoleId(this.jobRoleId)
        .careerYear(this.careerYear)
        .build();
  }
}
