package com.growit.common.config.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
  private final int expiredSecond;
  private final int refreshExpiredSecond;
  private final String secretKey;
}
