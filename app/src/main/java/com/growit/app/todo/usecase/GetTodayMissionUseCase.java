package com.growit.app.todo.usecase;

import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.ToDoRepository;
import com.growit.app.todo.domain.dto.GetDateQueryFilter;
import com.growit.app.todo.domain.util.ToDoUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTodayMissionUseCase {
  private final ToDoRepository toDoRepository;

  @Transactional(readOnly = true)
  public List<ToDo> execute(GetDateQueryFilter filter) {
    List<ToDo> toDoList = toDoRepository.findByUserIdAndDate(filter.userId(), filter.date());
    if (toDoList.isEmpty()) {
      return null;
    }
    return ToDoUtils.getNotCompletedToDos(toDoList);
  }
}
