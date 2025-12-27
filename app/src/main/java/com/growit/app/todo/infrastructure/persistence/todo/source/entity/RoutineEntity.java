package com.growit.app.todo.infrastructure.persistence.todo.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.todo.domain.vo.RepeatType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "routines")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineEntity extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, length = 128)
  private String userId;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private RepeatType repeatType;
}
