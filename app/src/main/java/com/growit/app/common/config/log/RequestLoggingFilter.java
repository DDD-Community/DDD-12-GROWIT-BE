package com.growit.app.common.config.log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // ELB Health Check 로깅 제외
    if (isHealthCheckRequest(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper cachingResponse = new ContentCachingResponseWrapper(response);

    String requestId = UUID.randomUUID().toString();
    MDC.put("requestId", requestId);

    long startTime = System.currentTimeMillis();

    try {
      filterChain.doFilter(cachingRequest, cachingResponse);
    } finally {
      long duration = System.currentTimeMillis() - startTime;

      Map<String, Object> logMap = new LinkedHashMap<>();
      logMap.put("timestamp", Instant.now().toString());
      logMap.put("requestId", requestId);
      logMap.put("method", request.getMethod());
      logMap.put("uri", request.getRequestURI());
      logMap.put("queryString", request.getQueryString());
      logMap.put("status", response.getStatus());
      logMap.put("elapsedTimeMs", duration);
      logMap.put("remoteAddr", request.getRemoteAddr());
      logMap.put("userAgent", request.getHeader("User-Agent"));

      // 요청 바디
      try {
        String requestBody =
            new String(cachingRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
        logMap.put("requestBody", maskSensitiveData(requestBody));
      } catch (Exception e) {
        logMap.put("requestBody", "unreadable");
      }

      // 응답 바디
      try {
        String responseBody =
            new String(cachingResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
        logMap.put("responseBody", maskSensitiveData(responseBody));
      } catch (Exception e) {
        logMap.put("responseBody", "unreadable");
      }

      log.info("{}", objectMapper.writeValueAsString(logMap));

      MDC.clear();
      cachingResponse.copyBodyToResponse();
    }
  }

  private boolean isHealthCheckRequest(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    String uri = request.getRequestURI();
    
    return "/actuator/health".equals(uri) && 
           userAgent != null && 
           userAgent.contains("ELB-HealthChecker");
  }

  private Object maskSensitiveData(String jsonBody) {
    try {
      JsonNode node = objectMapper.readTree(jsonBody);
      if (node.has("password")) ((ObjectNode) node).put("password", "****");
      return node;
    } catch (Exception e) {
      return jsonBody;
    }
  }
}
