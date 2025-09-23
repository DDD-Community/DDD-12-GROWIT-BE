package com.growit.app.advice.domain.mentor;

import java.util.Optional;

public interface MentorAdviceRepository {

  Optional<MentorAdvice> findByUserIdAndGoalId(String userId, String goalId);

  void save(MentorAdvice mentorAdvice);
}
