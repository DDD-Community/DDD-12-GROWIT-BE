package com.growit.app.todo.infrastructure.persistence.todo;

import com.growit.app.common.util.IDGenerator;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetCountByDateQueryFilter;
import com.growit.app.todo.domain.dto.GetDateRangeQueryFilter;
import com.growit.app.todo.domain.dto.GetToDoDateQueryFilter;
import com.growit.app.todo.domain.vo.Routine;
import com.growit.app.todo.infrastructure.persistence.todo.source.DBToDoRepository;
import com.growit.app.todo.infrastructure.persistence.todo.source.DBRoutineRepository;
import com.growit.app.todo.infrastructure.persistence.todo.source.entity.RoutineEntity;
import com.growit.app.todo.infrastructure.persistence.todo.source.entity.ToDoEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ToDoRepositoryImpl implements ToDoRepository {
  private final ToDoDBMapper mapper;
  private final DBToDoRepository repository;
  private final DBRoutineRepository routineRepository;

  @Override
  public void saveToDo(ToDo toDo) {
    String routineId = null;
    if (toDo.getRoutine() != null) {
      String routineUid = IDGenerator.generateId();
      RoutineEntity routineEntity = mapper.toRoutineEntity(toDo.getRoutine(), routineUid, toDo.getUserId());
      routineRepository.save(routineEntity);
      routineId = routineUid;
    }
    
    Optional<ToDoEntity> existing = repository.findByUid(toDo.getId());
    if (existing.isPresent()) {
      ToDoEntity entity = existing.get();
      entity.updateToByDomain(toDo);
      repository.save(entity);
    } else {
      ToDoEntity toDoEntity = mapper.toEntity(toDo, routineId);
      repository.save(toDoEntity);
    }
  }

  @Override
  public int countByDateQuery(GetCountByDateQueryFilter countByDateQueryFilter) {
    return repository.countByDateQuery(countByDateQueryFilter);
  }

  @Override
  public Optional<ToDo> findById(String id) {
    Optional<ToDoEntity> entity = repository.findByUid(id);
    if (entity.isEmpty()) {
      return Optional.empty();
    }
    
    ToDoEntity toDoEntity = entity.get();
    Routine routine = null;
    if (toDoEntity.getRoutineId() != null) {
      Optional<RoutineEntity> routineEntity = routineRepository.findByUidAndDeletedAtIsNull(toDoEntity.getRoutineId());
      if (routineEntity.isPresent()) {
        routine = mapper.routineEntityToDomain(routineEntity.get());
      }
    }
    
    return Optional.of(mapper.toDomain(toDoEntity, routine));
  }

  @Override
  public List<ToDo> findByUserIdAndDate(String userId, LocalDate today) {
    List<ToDoEntity> entities = repository.findByUserIdAndDate(userId, today);
    return entitiesToDomain(entities);
  }

  @Override
  public List<ToDo> findByGoalId(String goalId) {
    List<ToDoEntity> entities = repository.findByGoalId(goalId);
    return entitiesToDomain(entities);
  }

  @Override
  public List<ToDo> findByDateFilter(GetToDoDateQueryFilter filter) {
    List<ToDoEntity> entities = repository.findByDateFilter(filter);
    return entitiesToDomain(entities);
  }

  @Override
  public List<ToDo> findAllByUserIdAndCreatedAtBetween(
      String userId, LocalDateTime start, LocalDateTime end) {
    List<ToDoEntity> entities = repository.findAllByUserIdAndCreatedAtBetween(userId, start, end);
    return entitiesToDomain(entities);
  }

  @Override
  public List<ToDo> findByUserIdAndDateRange(GetDateRangeQueryFilter filter) {
    List<ToDoEntity> entities = repository.findByUserIdAndDateRange(filter);
    return entitiesToDomain(entities);
  }

  private List<ToDo> entitiesToDomain(List<ToDoEntity> entities) {
    return entities.stream()
        .map(this::entityToDomain)
        .toList();
  }

  private ToDo entityToDomain(ToDoEntity entity) {
    Routine routine = null;
    if (entity.getRoutineId() != null) {
      Optional<RoutineEntity> routineEntity = routineRepository.findByUidAndDeletedAtIsNull(entity.getRoutineId());
      if (routineEntity.isPresent()) {
        routine = mapper.routineEntityToDomain(routineEntity.get());
      }
    }
    return mapper.toDomain(entity, routine);
  }
}
