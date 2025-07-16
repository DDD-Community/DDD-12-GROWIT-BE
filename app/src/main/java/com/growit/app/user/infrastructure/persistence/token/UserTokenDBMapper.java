package com.growit.app.user.infrastructure.persistence.token;

import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.infrastructure.persistence.token.source.UserTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class UserTokenDBMapper {
  public UserTokenEntity toEntity(UserToken userToken) {
    return UserTokenEntity.builder()
        .uid(userToken.getId())
        .userId(userToken.getUserId())
        .token(userToken.getToken())
        .build();
  }

  public UserToken toDomain(UserTokenEntity entity) {
    if (entity == null) return null;
    return UserToken.builder()
        .id(entity.getUid())
        .userId(entity.getUserId())
        .token(entity.getToken())
        .build();
  }
}
