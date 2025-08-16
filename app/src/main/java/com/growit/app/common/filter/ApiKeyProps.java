package com.growit.app.common.filter;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record ApiKeyProps(List<String> apiKeys) {
  public ApiKeyProps {
    apiKeys = (apiKeys == null) ? List.of() : List.copyOf(apiKeys);
  }
}
