package com.growit.app.advice.batch.scheduler;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MorningAdviceScheduler {

  private final JobLauncher jobLauncher;
  private final Job morningAdviceJob;

  @Scheduled(cron = "0 0 7 * * *") // Daily at 7:00 AM
  public void runMorningAdviceJob() {
    log.info("Starting Morning Advice Batch Job at {}", LocalDateTime.now());
    try {
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("time", System.currentTimeMillis()) // Unique parameter to enforce run
              .toJobParameters();

      jobLauncher.run(morningAdviceJob, jobParameters);

    } catch (Exception e) {
      log.error("Failed to run Morning Advice Batch Job", e);
    }
  }
}
