package com.growit.app.todos.infrastructure.persistence.todos;

import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import com.growit.app.todos.infrastructure.persistence.todos.source.DBToDoRepository;
import com.growit.app.todos.infrastructure.persistence.todos.source.entity.ToDoEntity;
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
      ToDoEntity exist = existing.get();
      exist.updateToByDomain(toDo);
      repository.save(exist);
    } else {
      ToDoEntity toDoEntity = mapper.toEntity(toDo);
      repository.save(toDoEntity);
    }
  }

  @Override
  public int countByToDo(LocalDate date, String userId) {
    return repository.countByDateAndUserId(date, userId);
  }
}
