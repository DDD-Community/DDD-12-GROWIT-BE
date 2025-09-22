package com.growit.app.aikpts.usecase;

import com.growit.app.aikpts.domain.aikpts.service.AIKPTSService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAIKPTSUseCase {
  
  private final AIKPTSService aikptsService;

  public void execute(String id) {
    aikptsService.deleteAIKPTS(id);
  }
}
