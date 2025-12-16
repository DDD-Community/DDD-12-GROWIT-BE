package com.growit.app.goal.infrastructure.persistence.analysis;

import com.growit.app.goal.domain.anlaysis.AnalysisRepository;
import com.growit.app.goal.domain.anlaysis.GoalAnalysis;
import com.growit.app.goal.infrastructure.persistence.analysis.source.DBAnalysisRepository;
import com.growit.app.goal.infrastructure.persistence.analysis.source.entity.AnalysisEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnalysisRepositoryImpl implements AnalysisRepository {
  private final AnalysisDBMapper mapper;
  private final DBAnalysisRepository analysisRepository;

  @Override
  public Optional<GoalAnalysis> findByGoalId(String goalId) {
    Optional<AnalysisEntity> entity = analysisRepository.findByGoalId(goalId);
    return entity.map(mapper::toDomain);
  }

  @Override
  public void save(String goalId, GoalAnalysis analysis) {
    Optional<AnalysisEntity> existing = analysisRepository.findByGoalId(goalId);

    if (existing.isPresent()) {
      AnalysisEntity exist = existing.get();
      mapper.updateEntity(exist, analysis);
      analysisRepository.save(exist);
    } else {
      AnalysisEntity entity = mapper.toEntity(goalId, analysis);
      analysisRepository.save(entity);
    }
  }

  @Override
  public void deleteByGoalId(String goalId) {
    analysisRepository.deleteByGoalId(goalId);
  }
}
