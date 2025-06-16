package com.growit.app.user.infrastructure.persistence.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserJpaEntity toEntity(User domain) {
    return UserJpaEntity.builder()
      .uid(domain.getId())
      .email(domain.getEmail().value())
      .password(domain.getPassword())
      .name(domain.getName())
      .jobRoleId(domain.getJobRoleId())
      .careerYear(domain.getCareerYear())
      .build();
  }

  public User toDomain(UserJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return User.builder()
      .id(entity.getUid())
      .email(new Email(entity.getEmail()))
      .password(entity.getPassword())
      .name(entity.getName())
      .jobRoleId(entity.getJobRoleId())
      .careerYear(entity.getCareerYear())
      .build();
  }
}
