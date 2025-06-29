package com.growit.app.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**") // 모든 API 경로에 대해
        .allowedOrigins("*") // 허용할 프론트 주소, 우선 전체 허용
        .allowedMethods("*") // 허용할 메서드
        .allowedHeaders("*") // 모든 헤더 허용
        .allowCredentials(false) // 인증 정보(Cookie 등) 허용
        .maxAge(3600); // preflight 요청 결과를 캐시하는 시간(초)
  }
}
