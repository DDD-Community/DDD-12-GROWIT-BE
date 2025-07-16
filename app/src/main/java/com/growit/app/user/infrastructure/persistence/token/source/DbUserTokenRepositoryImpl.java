package com.growit.app.user.infrastructure.persistence.token.source;

import static com.growit.app.user.infrastructure.persistence.token.source.QUserTokenEntity.userTokenEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DbUserTokenRepositoryImpl implements DbUserTokenQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public DbUserTokenRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public Optional<UserTokenEntity> findByUserId(String userId) {
    return Optional.ofNullable(
        jpaQueryFactory
            .selectFrom(userTokenEntity)
            .where(userTokenEntity.userId.eq(userId))
            .fetchOne());
  }

  @Override
  public Optional<UserTokenEntity> findByToken(String token) {
    return Optional.ofNullable(
        jpaQueryFactory
            .selectFrom(userTokenEntity)
            .where(userTokenEntity.token.eq(token))
            .fetchOne());
  }

  @Override
  public Optional<UserTokenEntity> findByUid(String uid) {
    return Optional.ofNullable(
        jpaQueryFactory.selectFrom(userTokenEntity).where(userTokenEntity.uid.eq(uid)).fetchOne());
  }
}
