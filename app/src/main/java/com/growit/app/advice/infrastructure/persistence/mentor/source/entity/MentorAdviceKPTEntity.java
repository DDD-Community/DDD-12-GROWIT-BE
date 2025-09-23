package com.growit.app.advice.infrastructure.persistence.mentor.source.entity;

import com.growit.app.advice.domain.mentor.MentorAdvice;
import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "mentor_advice_kpts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MentorAdviceKPTEntity extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "mentor_advice_id", nullable = false)
    private MentorAdviceEntity mentorAdvice;

    @Column(nullable = false, length = 500)
    private String keep;

    @Column(nullable = false, length = 500)
    private String problem;

    @Column(name = "try_next", nullable = false, length = 500)
    private String tryNext;

    public void updateByDomain(MentorAdvice.Kpt kpt) {
        this.keep = kpt.getKeep();
        this.problem = kpt.getProblem();
        this.tryNext = kpt.getTryNext();
    }

    public MentorAdvice.Kpt toDomain() {
        return new MentorAdvice.Kpt(this.keep, this.problem, this.tryNext);
    }

    public static MentorAdviceKPTEntity from(MentorAdvice.Kpt kpt, MentorAdviceEntity mentorAdviceEntity) {
        return MentorAdviceKPTEntity.builder()
                .mentorAdvice(mentorAdviceEntity)
                .keep(kpt.getKeep())
                .problem(kpt.getProblem())
                .tryNext(kpt.getTryNext())
                .build();
    }
}
