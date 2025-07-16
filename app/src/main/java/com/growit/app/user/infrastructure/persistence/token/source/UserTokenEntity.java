package com.growit.app.user.infrastructure.persistence.token.source;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.token.UserToken;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@Table(name = "user_tokens")
public class UserTokenEntity extends BaseEntity {

  @Column(nullable = false, unique = true, length = 128)
  private String uid;

  @Column(nullable = false, unique = true, length = 512)
  private String token;

  @Column(nullable = false)
  private String userId;

  public void updateByDomain(UserToken token) {
    this.token = token.getToken();
  }
}
