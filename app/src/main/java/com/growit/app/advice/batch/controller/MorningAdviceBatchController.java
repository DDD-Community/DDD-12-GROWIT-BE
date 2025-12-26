package com.growit.app.advice.batch.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/advice/batch")
@RequiredArgsConstructor
public class MorningAdviceBatchController {

  private final JobLauncher jobLauncher;
  private final Job morningAdviceJob;

  @PostMapping("/morning")
  public ResponseEntity<String> runMorningAdviceJob() {
    log.info("Manual trigger for Morning Advice Batch Job at {}", LocalDateTime.now());
    try {
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("time", System.currentTimeMillis())
              .addString("trigger", "manual")
              .toJobParameters();

      jobLauncher.run(morningAdviceJob, jobParameters);

      return ResponseEntity.ok("Morning Advice Batch Job started successfully.");
    } catch (Exception e) {
      log.error("Failed to manual run Morning Advice Batch Job", e);
      return ResponseEntity.internalServerError()
          .body("Failed to run batch job: " + e.getMessage());
    }
  }
}
