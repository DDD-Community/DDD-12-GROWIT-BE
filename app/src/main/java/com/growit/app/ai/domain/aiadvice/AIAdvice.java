package com.growit.app.ai.domain.aiadvice;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AIAdvice {
    private String id;
    private String userId;
    private String goalId;
    private String promptId;
    private String templateUid;
    private Boolean success;
    private String errorMessage;
    private String adviceContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter(AccessLevel.NONE)
    private boolean isDeleted;

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public static AIAdvice fromEvent(String id, String userId, String goalId, String promptId, String templateUid, boolean success, String errorMessage, String adviceContent) {
        return AIAdvice.builder()
                .id(id)
                .userId(userId)
                .goalId(goalId)
                .promptId(promptId)
                .templateUid(templateUid)
                .success(success)
                .errorMessage(errorMessage)
                .adviceContent(adviceContent)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();
    }
}
