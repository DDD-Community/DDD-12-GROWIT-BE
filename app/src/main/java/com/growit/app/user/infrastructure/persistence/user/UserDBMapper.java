package com.growit.app.user.infrastructure.persistence.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.OAuth;
import com.growit.app.user.infrastructure.persistence.user.source.entity.OAuthAccountEntity;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserDBMapper {
  public UserEntity toEntity(User user) {
    UserEntity userEntity =
        UserEntity.builder()
            .uid(user.getId())
            .email(user.getEmail().value())
            .password(user.getPassword())
            .name(user.getName())
            .isOnboarding(user.isOnboarding())
            .oauthAccounts(new HashSet<>())
            .userPromotions(new HashSet<>())
            .build();

    Set<OAuthAccountEntity> oauthAccounts =
        user.getOauthAccounts().stream()
            .map(
                oauth ->
                    OAuthAccountEntity.builder()
                        .user(userEntity)
                        .provider(oauth.provider())
                        .providerId(oauth.providerId())
                        .refreshToken(oauth.refreshToken())
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
        .isDeleted(entity.getDeletedAt() != null)
        .isOnboarding(entity.getIsOnboarding())
        .oauthAccounts(
            new ArrayList<>(
                entity.getOauthAccounts().stream()
                    .map(
                        oauthEntity ->
                            new OAuth(
                                oauthEntity.getProvider(),
                                oauthEntity.getProviderId(),
                                oauthEntity.getRefreshToken()))
                    .toList()))
        .promotion(
            entity.getUserPromotions() != null && !entity.getUserPromotions().isEmpty()
                ? entity.getUserPromotions().stream()
                    .findFirst()
                    .map(userPromotion -> userPromotion.getPromotion().toDomain())
                    .orElse(null)
                : null)
        .saju(entity.getSajuInfo() != null ? entity.getSajuInfo().toDomain() : null)
        .build();
  }
}
