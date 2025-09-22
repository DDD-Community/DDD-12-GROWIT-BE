package com.growit.app.aikpts.domain.aikpts.service;

import com.growit.app.aikpts.domain.aikpts.AIKPTS;
import com.growit.app.aikpts.domain.aikpts.AIKPTSRepository;
import com.growit.app.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIKPTSService {
  private final AIKPTSRepository aikptsRepository;

  public AIKPTS getAIKPTSById(String id) {
    return aikptsRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("AIKPTS not found with id: " + id));
  }

  public List<AIKPTS> getAIKPTSByAiAdviceId(String aiAdviceId) {
    return aikptsRepository.findByAiAdviceId(aiAdviceId);
  }

  public void deleteAIKPTS(String id) {
    AIKPTS aikpts = getAIKPTSById(id);
    aikpts.delete();
    aikptsRepository.save(aikpts);
  }
}
