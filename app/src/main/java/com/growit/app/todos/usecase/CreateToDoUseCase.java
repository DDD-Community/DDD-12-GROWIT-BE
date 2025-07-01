package com.growit.app.todos.usecase;

import com.growit.app.todos.domain.CreateToDoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateToDoUseCase {

  @Transactional
  public String execute(CreateToDoCommand command) {
    return "";
  }
}
