package com.growit.app.aikpts.controller;

import com.growit.app.aikpts.controller.dto.response.AIKPTSResponse;
import com.growit.app.aikpts.controller.mapper.AIKPTSMapper;
import com.growit.app.aikpts.domain.aikpts.AIKPTS;
import com.growit.app.aikpts.usecase.DeleteAIKPTSUseCase;
import com.growit.app.aikpts.usecase.GetAIKPTSUseCase;
import com.growit.app.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai-kpts")
@RequiredArgsConstructor
public class AIKPTSController {
  
  private final GetAIKPTSUseCase getAIKPTSUseCase;
  private final DeleteAIKPTSUseCase deleteAIKPTSUseCase;
  private final AIKPTSMapper aikptsMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<AIKPTSResponse>> getAIKPTSById(
      @PathVariable String id) {
    
    AIKPTS aikpts = getAIKPTSUseCase.getAIKPTSById(id);
    AIKPTSResponse response = aikptsMapper.toResponse(aikpts);
    
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping("/ai-advice/{aiAdviceId}")
  public ResponseEntity<ApiResponse<List<AIKPTSResponse>>> getAIKPTSByAiAdviceId(
      @PathVariable String aiAdviceId) {
    
    List<AIKPTS> aikptsList = getAIKPTSUseCase.getAIKPTSByAiAdviceId(aiAdviceId);
    List<AIKPTSResponse> responseList = aikptsMapper.toResponseList(aikptsList);
    
    return ResponseEntity.ok(ApiResponse.success(responseList));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<String>> deleteAIKPTS(
      @PathVariable String id) {
    
    deleteAIKPTSUseCase.execute(id);
    
    return ResponseEntity.ok(ApiResponse.success("AIKPTS deleted successfully"));
  }
}
