package com.growit.app.aikpts.domain.aikpts;

import java.util.List;
import java.util.Optional;

public interface AIKPTSRepository {
  void save(AIKPTS aikpts);
  
  Optional<AIKPTS> findById(String id);
  
  List<AIKPTS> findByAiAdviceId(String aiAdviceId);
  
  /**
   * AI Advice ID로 기존 AIKPTS가 존재하는지 확인
   */
  boolean existsByAiAdviceId(String aiAdviceId);
  
  /**
   * 삭제되지 않은 AIKPTS만 조회
   */
  List<AIKPTS> findActiveByAiAdviceId(String aiAdviceId);
}
