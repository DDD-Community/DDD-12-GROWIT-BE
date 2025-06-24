package com.growit.app.goal.infrastructure.persistence.goal.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, length = 128)
  private String userId;

  @Column(nullable = false, length = 128)
  private String name;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false, length = 128)
  private String asIs;

  @Column(nullable = false, length = 128)
  private String toBe;

  @OneToMany(
      mappedBy = "goal",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  @Builder.Default
  private List<PlanEntity> plans = new ArrayList<>();
}

// insert into JOBROLE(UID, NAME, CREATED_AT, UPDATED_AT) VALUES ('uid1', 'developer', CURRENT_TIME,
// CURRENT_TIME);
//
// INSERT INTO GOALS(uid, name, start_date, end_date, as_is, to_be, created_at, updated_at, user_id)
// VALUES ('uid1', '내 목표는 그로잇 완성', '2025-06-23', '2025-07-20', '기획 정의', '배포 완료', CURRENT_TIME,
// CURRENT_TIME, 's7WxxfuosYXs38EDcy0Vm');
//
// INSERT INTO PLANS(id, goal_id, content, created_at, updated_at, uid)
// VALUES
//  ('1', 1, '기획 및 설계 회의', CURRENT_TIME, CURRENT_TIME, 'uid1'),
//  ('2', 1, '디자인 시안 뽑기', CURRENT_TIME, CURRENT_TIME, 'uid2'),
//  ('3', 1, '프론트 개발 및 백 개발 완료', CURRENT_TIME, CURRENT_TIME, 'uid3'),
//  ('4', 1, '배포 완료', CURRENT_TIME, CURRENT_TIME, 'uid4');
//
// select * from goals;
// select * from plans;
