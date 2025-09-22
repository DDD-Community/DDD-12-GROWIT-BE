package com.growit.app.aikpts.usecase;

import com.growit.app.aikpts.domain.aikpts.AIKPTS;
import com.growit.app.aikpts.domain.aikpts.service.AIKPTSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAIKPTSUseCase {
  
  private final AIKPTSService aikptsService;

  public AIKPTS getAIKPTSById(String id) {
    return aikptsService.getAIKPTSById(id);
  }

  public List<AIKPTS> getAIKPTSByAiAdviceId(String aiAdviceId) {
    return aikptsService.getAIKPTSByAiAdviceId(aiAdviceId);
  }
}
