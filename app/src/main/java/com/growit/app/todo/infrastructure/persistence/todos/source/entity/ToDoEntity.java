package com.growit.app.todo.infrastructure.persistence.todos.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.todo.domain.ToDo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
  private String planId;

  @Column(nullable = false)
  private String goalId;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false) // 5자이상 30자이내로 하고 싶음
  private String content;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private boolean isCompleted;

  public void updateToByDomain(ToDo toDo) {
    this.planId = toDo.getPlanId();
    this.date = toDo.getDate();
    this.content = toDo.getContent();
    this.isCompleted = toDo.isCompleted();
    if (toDo.isDeleted()) {
      this.setDeletedAt(LocalDateTime.now());
    }
  }
}
