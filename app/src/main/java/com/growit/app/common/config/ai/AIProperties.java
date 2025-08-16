package com.growit.app.common.config.ai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai.api")
public class AIProperties {
  private String apiKey;
  private String baseUrl = "https://api.openai.com";
  private String model = "gpt-4o-mini";
  private double temperature = 0.3;
}
