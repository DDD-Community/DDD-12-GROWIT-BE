package com.growit.app.advice.infrastructure.persistence.mentor.source;

import com.growit.app.advice.infrastructure.persistence.mentor.source.entity.MentorAdviceEntity;
import java.util.List;
import java.util.Optional;

public interface MentorAdviceCustomRepository {

  Optional<MentorAdviceEntity> findByUserIdAndGoalId(String userId, String goalId);

  List<MentorAdviceEntity> findByUserId(String userId);
}
