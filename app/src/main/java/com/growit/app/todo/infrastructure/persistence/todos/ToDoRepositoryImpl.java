package com.growit.app.todo.infrastructure.persistence.todos;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.infrastructure.persistence.todos.source.DBToDoRepository;
import com.growit.app.todo.infrastructure.persistence.todos.source.entity.ToDoEntity;
import java.time.LocalDate;
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
  public int countByToDo(LocalDate date, String userId, String planId) {
    return repository.countByDateAndUserIdAndPlanId(date, userId, planId);
  }

  @Override
  public int countByToDoWithToDoId(LocalDate date, String userId, String planId, String id) {
    return repository.countByDateAndUserIdAndPlanIdAndUidNot(date, userId, planId, id);
  }

  @Override
  public Optional<ToDo> findById(String id) {
    Optional<ToDoEntity> entity = repository.findByUid(id);
    return entity.map(mapper::toDomain);
  }
}
