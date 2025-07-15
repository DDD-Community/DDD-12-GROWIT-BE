package com.growit.app.user.infrastructure.persistence.user.source;

import static com.growit.app.user.infrastructure.persistence.user.source.entity.QUserEntity.userEntity;

import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBUserQueryRepositoryImpl implements DBUserQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(userEntity)
            .where(userEntity.email.eq(email), userEntity.deletedAt.isNull())
            .fetchOne());
  }

  @Override
  public Optional<UserEntity> findByUid(String uid) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(userEntity)
            .where(userEntity.uid.eq(uid), userEntity.deletedAt.isNull())
            .fetchOne());
  }
}
