package com.growit.app.todo.infrastructure.persistence.todos.source;

import static com.growit.app.todo.infrastructure.persistence.todos.source.entity.QToDoEntity.toDoEntity;

import com.growit.app.todo.infrastructure.persistence.todos.source.entity.QToDoEntity;
import com.growit.app.todo.infrastructure.persistence.todos.source.entity.ToDoEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DBToDoQueryRepositoryImpl implements DBToDoQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<ToDoEntity> findByUid(String uid) {
    return Optional.ofNullable(
      queryFactory
        .selectFrom(toDoEntity)
        .where(toDoEntity.uid.eq(uid), toDoEntity.deletedAt.isNull())
        .fetchOne());
  }

  @Override
  public List<ToDoEntity> findByUserIdAndGoalIdAndPlanId(String userId, String goalId, String planId) {
    QToDoEntity toDo = QToDoEntity.toDoEntity;
    return queryFactory
      .selectFrom(toDo)
      .where(
        toDo.userId.eq(userId),
        toDo.goalId.eq(goalId),
        toDo.planId.eq(planId),
        toDo.deletedAt.isNull()
      )
      .fetch();
  }
}
