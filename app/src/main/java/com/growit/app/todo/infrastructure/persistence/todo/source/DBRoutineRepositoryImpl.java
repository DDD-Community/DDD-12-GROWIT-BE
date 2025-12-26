package com.growit.app.todo.infrastructure.persistence.todo.source;

import static com.growit.app.todo.infrastructure.persistence.todo.source.entity.QRoutineEntity.routineEntity;

import com.growit.app.todo.infrastructure.persistence.todo.source.entity.RoutineEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBRoutineRepositoryImpl implements DBRoutineRepository {

  private final JPAQueryFactory queryFactory;
  private final EntityManager entityManager;

  @Override
  public Optional<RoutineEntity> findByUidAndDeletedAtIsNull(String uid) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(routineEntity)
            .where(routineEntity.uid.eq(uid), routineEntity.deletedAt.isNull())
            .fetchOne());
  }

  @Override
  public void save(RoutineEntity entity) {
    if (entity.getId() == null) {
      entityManager.persist(entity);
    } else {
      entityManager.merge(entity);
    }
  }
}