package com.growit.app.goal.infrastructure.persistence.goal.source;

import com.growit.app.goal.domain.goal.vo.GoalUpdateStatus;
import com.growit.app.goal.infrastructure.persistence.goal.source.entity.GoalEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;

public interface DBGoalQueryRepository {
  List<GoalEntity> findByUserId(String userId);

  Optional<GoalEntity> findByUidAndUserId(String uid, String userId);

  Optional<GoalEntity> findByUserIdAndGoalDuration(String userId, LocalDate today);

  List<String> findEndedCandidateUids(
      LocalDate today, GoalUpdateStatus updateStatus, PageRequest pageRequest);

  List<GoalEntity> findByUidIn(List<String> uids);

  void flushAndClear();
}
