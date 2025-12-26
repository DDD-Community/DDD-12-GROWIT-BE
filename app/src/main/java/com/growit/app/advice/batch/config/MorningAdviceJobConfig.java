package com.growit.app.advice.batch.config;

import com.growit.app.advice.batch.processor.MorningAdviceProcessor;
import com.growit.app.advice.batch.reader.ActiveUserReader;
import com.growit.app.advice.batch.writer.MorningAdviceWriter;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceClient;
import com.growit.app.advice.domain.chatadvice.service.ChatAdviceDataCollector;
import com.growit.app.advice.usecase.dto.ai.ChatAdviceRequest;
import com.growit.app.user.domain.user.UserRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MorningAdviceJobConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final UserRepository userRepository;
  private final com.growit.app.user.infrastructure.persistence.user.source.DBUserRepository
      dbUserRepository;
  private final ChatAdviceRepository chatAdviceRepository;
  private final ChatAdviceDataCollector chatAdviceDataCollector;
  private final ChatAdviceClient chatAdviceClient;
  private final java.time.Clock clock;

  @Bean
  public Job morningAdviceJob() {
    return new JobBuilder("morningAdviceJob", jobRepository).start(morningAdviceStep()).build();
  }

  @Bean
  public Step morningAdviceStep() {
    return new StepBuilder("morningAdviceStep", jobRepository)
        .<com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity,
            ChatAdviceRequest>
            chunk(10, transactionManager)
        .reader(morningAdviceUserReader())
        .processor(morningAdviceProcessor())
        .writer(morningAdviceWriter())
        .build();
  }

  @Bean
  @StepScope
  public ItemReader<com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity>
      morningAdviceUserReader() {
    DayOfWeek dayOfWeek = LocalDate.now(clock).getDayOfWeek();
    log.info("Initializing MorningAdviceUserReader for day: {}", dayOfWeek);

    if (dayOfWeek == DayOfWeek.MONDAY) {
      Map<String, Sort.Direction> sorts = new HashMap<>();
      sorts.put("uid", Sort.Direction.ASC);

      return new RepositoryItemReaderBuilder<
              com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity>()
          .name("mondayUserReader")
          .repository(dbUserRepository)
          .methodName("findAll")
          .pageSize(10)
          .sorts(sorts)
          .build();
    } else {
      return new ActiveUserReader(chatAdviceRepository, dbUserRepository, clock);
    }
  }

  @Bean
  public MorningAdviceProcessor morningAdviceProcessor() {
    return new MorningAdviceProcessor(chatAdviceDataCollector);
  }

  @Bean
  public MorningAdviceWriter morningAdviceWriter() {
    return new MorningAdviceWriter(chatAdviceClient, chatAdviceRepository);
  }
}
