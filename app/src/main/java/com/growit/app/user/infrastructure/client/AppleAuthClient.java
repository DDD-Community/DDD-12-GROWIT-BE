package com.growit.app.user.infrastructure.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.config.oauth.AppleClientSecretGenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleAuthClient {

  private final AppleClientSecretGenerator appleClientSecretGenerator;
  private final RestTemplateBuilder restTemplateBuilder;
  private final ObjectMapper objectMapper;

  @Value("${app.oauth.apple.client-id}")
  private String clientId;

  private static final String APPLE_TOKEN_URL = "https://appleid.apple.com/auth/token";

  private RestTemplate restTemplate;

  @PostConstruct
  public void init() {
    this.restTemplate = restTemplateBuilder.build();
  }

  public String getRefreshToken(String authorizationCode) {
    try {
      String clientSecret = appleClientSecretGenerator.createClientSecret();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add("client_id", clientId);
      body.add("client_secret", clientSecret);
      body.add("code", authorizationCode);
      body.add("grant_type", "authorization_code");

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

      ResponseEntity<String> response =
          restTemplate.exchange(APPLE_TOKEN_URL, HttpMethod.POST, request, String.class);

      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        AppleTokenResponse tokenResponse =
            objectMapper.readValue(response.getBody(), AppleTokenResponse.class);
        log.info("Apple authorization_code 교환 성공: refreshToken 발급 완료");
        return tokenResponse.getRefreshToken();
      } else {
        log.error("Apple authorization_code 교환 실패: status={}, body={}", response.getStatusCode(), response.getBody());
        throw new IllegalStateException("Apple 인가 코드(authorization_code) 교환에 실패했습니다. 코드가 만료되었거나 이미 사용된 코드입니다.");
      }
    } catch (IllegalStateException e) {
      throw e;
    } catch (Exception e) {
      log.error("Apple Token API 통신 중 예외 발생: exceptionType={}, message={}", e.getClass().getSimpleName(), e.getMessage(), e);
      throw new IllegalStateException("Apple 인증 서버와의 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.", e);
    }
  }
}
