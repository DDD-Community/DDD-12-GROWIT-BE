package com.growit.app.common.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
  @Bean
  public WebClient webClient(WebClient.Builder builder) {
    // AI 분석은 시간이 오래 걸릴 수 있으므로 충분한 타임아웃 설정
    HttpClient httpClient =
        HttpClient.create()
            .responseTimeout(Duration.ofMinutes(5)) // 응답 타임아웃 5분
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000); // 연결 타임아웃 30초

    return builder.clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }
}
