package com.growit.app.user.infrastructure.persistence.user.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.CareerYear;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
    fetch = FetchType.EAGER  // 이렇게 추가
  )
  private Set<OAuthAccountEntity> oauthAccounts = new HashSet<>();

  public void updateByDomain(User user) {
    this.name = user.getName();
    this.jobRoleId = user.getJobRoleId();
    this.careerYear = user.getCareerYear();
    this.isOnboarding = user.isOnboarding();
    if (user.isDeleted()) {
      this.setDeletedAt(LocalDateTime.now());
    }
  }
}
