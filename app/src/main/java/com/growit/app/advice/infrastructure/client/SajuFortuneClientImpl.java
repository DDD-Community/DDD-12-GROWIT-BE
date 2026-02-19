package com.growit.app.advice.infrastructure.client;

import com.growit.app.advice.domain.saju.service.SajuFortuneClient;
import com.growit.app.advice.infrastructure.dto.ForceTellerRequest;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class SajuFortuneClientImpl implements SajuFortuneClient {

  private final WebClient webClient;

  @Value("${ai.mentor.url}")
  private String nestApiUrl;

  @PostConstruct
  public void init() {
    if (nestApiUrl != null) {
      this.nestApiUrl = this.nestApiUrl.trim();
      log.info("Saju Fortune Client URL 초기화: '{}'", nestApiUrl);
    } else {
      log.warn("Saju Fortune Client URL이 설정되지 않았습니다.");
    }
  }

  @Override
  public Map<String, Object> getFortune(String birthDate, String birthTime, String gender) {
    ForceTellerRequest request = convertToForceTellerRequest(birthDate, birthTime, gender);
    String fullUrl = nestApiUrl + "/forceteller/saju";

    return webClient
        .post()
        .uri(fullUrl)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        .block();
  }

  @Override
  public Map<String, String> getManse(String birthDate, String birthTime, String gender) {
    ForceTellerRequest request = convertToForceTellerRequest(birthDate, birthTime, gender);
    String fullUrl = nestApiUrl + "/forceteller/saju";

    Map<String, Object> responseMap =
        webClient
            .post()
            .uri(fullUrl)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();

    if (responseMap == null) {
      return Map.of();
    }

    log.info("Manse Response: {}", responseMap);

    Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
    if (data == null) {
      return Map.of();
    }

    return Map.of(
        "year", String.valueOf(data.get("year")),
        "month", String.valueOf(data.get("month")),
        "day", String.valueOf(data.get("day")),
        "time", String.valueOf(data.get("hour")));
  }

  private ForceTellerRequest convertToForceTellerRequest(
      String birthDate, String birthTime, String gender) {
    LocalDate date = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    LocalTime time = LocalTime.parse(birthTime, DateTimeFormatter.ofPattern("HH:mm"));

    String formattedBirthDate = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

    return ForceTellerRequest.builder()
        .name("User")
        .gender(gender)
        .calendar("S")
        .birthday(formattedBirthDate)
        .birthtime(birthTime)
        .hmUnsure(false)
        .midnightAdjust(true)
        .year(date.getYear())
        .month(date.getMonthValue())
        .day(date.getDayOfMonth())
        .hour(time.getHour())
        .min(time.getMinute())
        .build();
  }
}
