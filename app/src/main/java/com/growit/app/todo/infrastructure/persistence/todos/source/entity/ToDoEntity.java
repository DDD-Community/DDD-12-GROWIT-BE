package com.growit.app.todo.infrastructure.persistence.todos.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.todo.domain.ToDo;
import com.growit.app.todo.domain.vo.RepeatType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "todos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoEntity extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, length = 128)
  private String userId;


  @Column(nullable = false)
  private String goalId;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false) // 5자이상 30자이내로 하고 싶음
  private String content;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private boolean isCompleted;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private boolean isImportant;

  // Routine fields - stored as separate columns for easier querying
  @Column
  private LocalDate routineStartDate;

  @Column
  private LocalDate routineEndDate;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private RepeatType routineRepeatType;

  public void updateToByDomain(ToDo toDo) {
    this.date = toDo.getDate();
    this.content = toDo.getContent();
    this.isCompleted = toDo.isCompleted();
    this.isImportant = toDo.isImportant();
    
    if (toDo.getRoutine() != null) {
      this.routineStartDate = toDo.getRoutine().getDuration().getStartDate();
      this.routineEndDate = toDo.getRoutine().getDuration().getEndDate();
      this.routineRepeatType = toDo.getRoutine().getRepeatType();
    } else {
      this.routineStartDate = null;
      this.routineEndDate = null;
      this.routineRepeatType = null;
    }
    
    if (toDo.isDeleted()) {
      this.setDeletedAt(LocalDateTime.now());
    }
  }
}
