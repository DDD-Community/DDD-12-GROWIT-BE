package com.growit.app.batch.controller;

import com.growit.app.batch.domain.batchjob.BatchJob;
import com.growit.app.batch.domain.service.AIBatchService;
import com.growit.app.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
@Slf4j
public class BatchController {
  
  private final AIBatchService aiBatchService;


  /**
   * 수동 배치 테스트 엔드포인트
   */
  @PostMapping("/daily-advice")
  public ResponseEntity<Map<String, Object>> triggerDailyAdvice() {
    log.info("Manual daily advice batch trigger received");
    
    try {
      BatchJob batchJob = aiBatchService.executeDailyAdviceGeneration();
      
      boolean isSuccess = batchJob.getFailureCount() == 0;
      
      return ResponseEntity.ok(Map.of(
          "success", isSuccess,
          "batchJob", batchJob,
          "message", isSuccess ? "Daily advice generation completed successfully" : "Daily advice generation completed with failures"
      ));
      
    } catch (Exception e) {
      log.error("Manual daily advice trigger failed", e);
      return ResponseEntity.internalServerError()
          .body(Map.of(
              "success", false,
              "error", e.getMessage(),
              "message", "Daily advice generation failed"
          ));
    }
  }

  @PostMapping("/weekly-plan")
  public ResponseEntity<Map<String, Object>> triggerWeeklyPlan() {
    log.info("Manual weekly plan batch trigger received");
    
    try {
      BatchJob batchJob = aiBatchService.executeWeeklyPlanRecommendation();
      
      boolean isSuccess = batchJob.getFailureCount() == 0;
      
      return ResponseEntity.ok(Map.of(
          "success", isSuccess,
          "batchJob", batchJob,
          "message", isSuccess ? "Weekly plan recommendation completed successfully" : "Weekly plan recommendation completed with failures"
      ));
      
    } catch (Exception e) {
      log.error("Manual weekly plan trigger failed", e);
      return ResponseEntity.internalServerError()
          .body(Map.of(
              "success", false,
              "error", e.getMessage(),
              "message", "Weekly plan recommendation failed"
          ));
    }
  }
}
