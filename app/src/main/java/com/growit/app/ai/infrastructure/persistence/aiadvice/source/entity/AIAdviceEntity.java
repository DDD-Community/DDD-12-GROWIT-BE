package com.growit.app.ai.infrastructure.persistence.aiadvice.source.entity;

import com.growit.app.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_advices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIAdviceEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String uid;

    @Column(nullable = false, length = 128)
    private String userId;

    @Column(length = 128)
    private String goalId;

    @Column(nullable = false, length = 128)
    private String promptId;

    @Column(nullable = false, length = 128)
    private String templateUid;

    @Column(nullable = false)
    private Boolean success;

    @Column(length = 1000)
    private String errorMessage;

    @Column(columnDefinition = "TEXT")
    private String adviceContent;
}
