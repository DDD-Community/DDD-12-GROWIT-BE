package com.growit.app.user.infrastructure.persistence.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;
import com.growit.app.user.infrastructure.persistence.user.source.entity.OAuthAccountEntity;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserDBMapper {
  public UserEntity toEntity(User user) {
    UserEntity userEntity = UserEntity.builder()
        .uid(user.getId())
        .email(user.getEmail().value())
        .password(user.getPassword())
        .name(user.getName())
        .jobRoleId(user.getJobRoleId())
        .careerYear(user.getCareerYear())
        .isOnboarding(user.isOnboarding())
        .build();

    Set<OAuthAccountEntity> oauthAccounts = user.getOauthAccounts().stream()
        .map(oauth -> OAuthAccountEntity.builder()
            .user(userEntity)
            .provider(oauth.provider())
            .providerId(oauth.providerId())
            .build())
        .collect(Collectors.toSet());

    userEntity.getOauthAccounts().addAll(oauthAccounts);
    return userEntity;
  }

  public User toDomain(UserEntity entity) {
    if (entity == null) return null;
    return User.builder()
        .id(entity.getUid())
        .email(new Email(entity.getEmail()))
        .password(entity.getPassword())
        .name(entity.getName())
        .jobRoleId(entity.getJobRoleId())
        .careerYear(entity.getCareerYear())
        .isDeleted(entity.getDeletedAt() != null)
        .isOnboarding(entity.getIsOnboarding())
        .oauthAccounts(entity.getOauthAccounts().stream()
            .map(oauthEntity -> new OAuth(oauthEntity.getProvider(), oauthEntity.getProviderId()))
            .toList())
        .build();
  }
}
