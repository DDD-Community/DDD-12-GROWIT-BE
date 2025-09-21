package com.growit.app.batch.domain.batchjob;

import com.growit.app.batch.domain.batchjob.vo.BatchJobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BatchJob {
  private String id;
  private String jobName;
  private BatchJobStatus status;
  private LocalDateTime startedAt;
  private LocalDateTime completedAt;
  private String errorMessage;
  private int processedCount;
  private int successCount;
  private int failureCount;

  public void start() {
    this.status = BatchJobStatus.RUNNING;
    this.startedAt = LocalDateTime.now();
  }

  public void complete(int processed, int success, int failure) {
    this.status = BatchJobStatus.COMPLETED;
    this.completedAt = LocalDateTime.now();
    this.processedCount = processed;
    this.successCount = success;
    this.failureCount = failure;
  }

  public void fail(String errorMessage) {
    this.status = BatchJobStatus.FAILED;
    this.completedAt = LocalDateTime.now();
    this.errorMessage = errorMessage;
  }
}
