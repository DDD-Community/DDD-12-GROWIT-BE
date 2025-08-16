package com.growit.app.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

  private static final String HEADER = "X-API-KEY";
  private final ApiKeyProps props;

  // 보호할 엔드포인트만 명시 (경로 + HTTP 메서드)
  private final List<RequestMatcher> protectedMatchers =
      List.of(new AntPathRequestMatcher("/goals/utils/ended", "GET"));

  @Override
  protected boolean shouldNotFilter(HttpServletRequest req) {
    // protectedMatchers 중 하나라도 매칭되면 필터 작동
    boolean isProtected = protectedMatchers.stream().anyMatch(m -> m.matches(req));
    // 보호 대상이 아니면 필터 미작동 (skip)
    return !isProtected;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    // CORS preflight는 보통 통과
    if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
      chain.doFilter(req, res);
      return;
    }

    String key = req.getHeader(HEADER);
    boolean valid =
        key != null && props.apiKeys().stream().anyMatch(cfg -> constantTimeEquals(cfg, key));

    if (!valid) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.setContentType("application/json");
      res.getWriter()
          .write("{\"code\":\"unauthorized\",\"message\":\"Missing or invalid API key\"}");
      return;
    }
    chain.doFilter(req, res);
  }

  private boolean constantTimeEquals(String a, String b) {
    return MessageDigest.isEqual(
        a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
  }
}
