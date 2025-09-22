package com.growit.app.aikpts.infrastructure.persistence.aikpts.source.entity;

import com.growit.app.aikpts.domain.aikpts.AIKPTS;
import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_kpts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIKPTSEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String uid;

  @Column(nullable = false, length = 128)
  private String aiAdviceId;

  @Column(columnDefinition = "TEXT")
  private String keeps;

  @Column(columnDefinition = "TEXT")
  private String problems;

  @Column(columnDefinition = "TEXT")
  private String trys;

  public void updateByDomain(AIKPTS aikpts) {
    this.aiAdviceId = aikpts.getAiAdviceId();
    this.keeps = String.join("|", aikpts.getKpt().getKeeps());
    this.problems = String.join("|", aikpts.getKpt().getProblems());
    this.trys = String.join("|", aikpts.getKpt().getTrys());
    
    if (aikpts.isDeleted()) {
      this.setDeletedAt(LocalDateTime.now());
    }
  }
}
