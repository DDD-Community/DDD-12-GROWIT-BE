package com.growit.app.user.infrastructure.persistence.user.source.entity;

import com.growit.app.user.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "oauth_account",
       uniqueConstraints = @UniqueConstraint(name = "uq_oauth_provider", columnNames = {"provider","provider_id"}))
@Getter
@Setter
public class OAuthAccount {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false) private String provider;
  @Column(name = "provider_id", nullable = false) private String providerId;

  private String email;
  private String nickname;

  @Column(name = "profile_image_url")
  private String profileImageUrl;
}
