package com.growit.app.todos.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.goal.domain.goal.GoalRepository;
import com.growit.app.todos.domain.ToDo;
import com.growit.app.todos.domain.ToDoRepository;
import com.growit.app.todos.domain.dto.UpdateToDoCommand;
import com.growit.app.todos.domain.service.ToDoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateToDoUseCase {
  private final ToDoValidator toDoValidator;
  private final ToDoRepository toDoRepository;
  private final GoalRepository goalRepository;

  @Transactional
  public void execute(UpdateToDoCommand command) {
    // NOTE ::
    //  1. planId까지 같이 조회해서 실제 그 주차에있는지 검증을 해야할까?
    //  2. plan간 업데이트가 있을 수 있을까? >> O
    //  3. date만 받고 알아서 plan을 계산해야할까? >> date(연산해서 알아서 찾는걸로) => Plan(duration) start, end 생성 => id조회
    // 주간 변경 해당

    // Alarm =>
    //    if (planRepository.findById(command.planId()).isEmpty()) {
    //      throw new NotFoundException("존재하지 않는 주차입니다.");
    //    }

    // TODO :: goal => filterByDate(command.date) => 어느 주차인지 계산  된걸 ToDo에 담기
    //  1. 의존성 관계 : ToDo는 GoalId Goal => ToDo 모름 => useCase GoalRepository를 알아도됨

    // 10 개
    ToDo toDo =
        toDoRepository
            .findById(command.id())
            .orElseThrow(() -> new NotFoundException("할 일 정보가 존재하지 않습니다."));

    //
    toDoValidator.isDateInRange(command.date(), command.planId());

    // planId => 넣어주면된다
    toDoValidator.tooManyToDoCreated(command.date(), command.userId(), command.planId());

    toDo.updateBy(command);
    toDoRepository.saveToDo(toDo);
  }
}
