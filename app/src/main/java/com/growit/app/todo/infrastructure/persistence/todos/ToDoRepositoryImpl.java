package com.growit.app.todo.infrastructure.persistence.todos;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetCountByDateQueryFilter;
import com.growit.app.todo.domain.dto.GetDateRangeQueryFilter;
import com.growit.app.todo.domain.dto.GetToDoDateQueryFilter;
import com.growit.app.todo.infrastructure.persistence.todos.source.DBToDoRepository;
import com.growit.app.todo.infrastructure.persistence.todos.source.entity.ToDoEntity;
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

  @Override
  public void saveToDo(ToDo toDo) {
    Optional<ToDoEntity> existing = repository.findByUid(toDo.getId());
    if (existing.isPresent()) {
      ToDoEntity entity = existing.get();
      entity.updateToByDomain(toDo);
      repository.save(entity);
    } else {
      ToDoEntity toDoEntity = mapper.toEntity(toDo);
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
    return entity.map(mapper::toDomain);
  }


  @Override
  public List<ToDo> findByUserIdAndDate(String userId, LocalDate today) {
    List<ToDoEntity> entities = repository.findByUserIdAndDate(userId, today);
    return mapper.toDomainList(entities);
  }

  @Override
  public List<ToDo> findByGoalId(String goalId) {
    List<ToDoEntity> entities = repository.findByGoalId(goalId);
    return mapper.toDomainList(entities);
  }

  @Override
  public List<ToDo> findByDateFilter(GetToDoDateQueryFilter filter) {
    List<ToDoEntity> entities = repository.findByDateFilter(filter);
    return mapper.toDomainList(entities);
  }

  @Override
  public List<ToDo> findAllByUserIdAndCreatedAtBetween(
      String userId, LocalDateTime start, LocalDateTime end) {
    List<ToDoEntity> entities = repository.findAllByUserIdAndCreatedAtBetween(userId, start, end);
    return mapper.toDomainList(entities);
  }

  @Override
  public List<ToDo> findByUserIdAndDateRange(GetDateRangeQueryFilter filter) {
    List<ToDoEntity> entities = repository.findByUserIdAndDateRange(filter);
    return mapper.toDomainList(entities);
  }
}
