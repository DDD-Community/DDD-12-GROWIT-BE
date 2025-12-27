package com.growit.app.todo.infrastructure.persistence.todo.source;

import com.growit.app.todo.infrastructure.persistence.todo.source.entity.RoutineEntity;
import java.util.Optional;

public interface DBRoutineRepository {
  Optional<RoutineEntity> findByUidAndDeletedAtIsNull(String uid);

  void save(RoutineEntity routineEntity);
}
