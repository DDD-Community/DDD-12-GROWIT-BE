package com.growit.app.aikpts.infrastructure.persistence.aikpts.source;

import com.growit.app.aikpts.infrastructure.persistence.aikpts.source.entity.AIKPTSEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DBAIKPTSRepository extends JpaRepository<AIKPTSEntity, Long> {
  
  Optional<AIKPTSEntity> findByUid(String uid);
  
  List<AIKPTSEntity> findByAiAdviceId(String aiAdviceId);
}
