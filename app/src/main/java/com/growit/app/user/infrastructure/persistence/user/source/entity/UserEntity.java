package com.growit.app.user.infrastructure.persistence.user.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.user.domain.user.vo.CareerYear;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(name = "job_role_id", nullable = false)
  private String jobRoleId;

  @Enumerated(EnumType.STRING)
  @Column(name = "career_year", nullable = false)
  private CareerYear careerYear;
}
