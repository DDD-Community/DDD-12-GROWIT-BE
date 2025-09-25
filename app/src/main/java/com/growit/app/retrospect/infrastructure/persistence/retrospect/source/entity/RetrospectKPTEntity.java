package com.growit.app.retrospect.infrastructure.persistence.retrospect.source.entity;

import com.growit.app.common.entity.BaseEntity;
import com.growit.app.retrospect.domain.retrospect.vo.KPT;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "retrospect_kpts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetrospectKPTEntity extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "retrospect_id", nullable = false)
  private RetrospectEntity retrospect;

  @Column(nullable = false, length = 500)
  private String keep;

  @Column(nullable = false, length = 500)
  private String problem;

  @Column(nullable = false, length = 500)
  private String tryNext;

  public void updateByDomain(KPT kpt) {
    this.keep = kpt.keep();
    this.problem = kpt.problem();
    this.tryNext = kpt.tryNext();
  }

  public KPT toDomain() {
    return new KPT(this.keep, this.problem, this.tryNext);
  }
}
