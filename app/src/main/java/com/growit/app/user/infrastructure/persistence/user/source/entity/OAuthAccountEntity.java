package com.growit.app.user.infrastructure.persistence.user.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "oauth_accounts",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_provider_pid",
          columnNames = {"provider", "provider_id"}),
      @UniqueConstraint(
          name = "uk_user_provider",
          columnNames = {"user_id", "provider"})
    })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OAuthAccountEntity extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(nullable = false)
  private String provider;

  @Column(name = "provider_id", nullable = false)
  private String providerId;
}
