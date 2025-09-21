package com.growit.app.common.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
// import software.amazon.awssdk.regions.Region;
// import software.amazon.awssdk.services.eventbridge.EventBridgeClient;

@Configuration
public class EventBridgeConfig {

  @Value("${aws.region:ap-northeast-2}")
  private String region;

  @Value("${aws.eventbridge.source:growit.spring}")
  private String eventSource;

  // TODO: 실제 EventBridge 연동 시 주석 해제
  /*
  @Bean
  public EventBridgeClient eventBridgeClient() {
    return EventBridgeClient.builder()
        .region(Region.of(region))
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build();
  }
  */

  @Bean
  public String eventSource() {
    return eventSource;
  }
}
