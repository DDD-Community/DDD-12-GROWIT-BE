package com.growit.app.user.infrastructure.persistence.user.source.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "oauth_accounts",
       uniqueConstraints = @UniqueConstraint(name = "uq_oauth_provider", columnNames = {"provider","provider_id"}))
@Getter
@Setter
public class OAuthAccountEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String uid;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private String provider;
  @Column(name = "provider_id", nullable = false)
  private String providerId;
}
