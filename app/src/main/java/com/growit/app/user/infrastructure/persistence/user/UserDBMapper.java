package com.growit.app.user.infrastructure.persistence.user;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDBMapper {
  public UserEntity toEntity(User user) {
    return UserEntity.builder()
        .uid(user.getId())
        .email(user.getEmail().value())
        .password(user.getPassword())
        .name(user.getName())
        .jobRoleId(user.getJobRoleId())
        .careerYear(user.getCareerYear())
        .build();
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
        .build();
  }
}
