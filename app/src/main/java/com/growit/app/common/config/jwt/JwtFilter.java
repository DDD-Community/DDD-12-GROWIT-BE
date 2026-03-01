package com.growit.app.common.config.jwt;

import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.exception.ExpiredTokenException;
import com.growit.app.user.domain.token.service.exception.TokenNotFoundException;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private static final String PROMOTION_REGISTER_URI = "/users/myprofile/promotion";

  private final TokenGenerator tokenGenerator;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) {
    try {
      String uri = request.getRequestURI();
      if ("/".equals(uri)
          || uri.startsWith("/auth")
          || uri.startsWith("/test")
          || uri.startsWith("/login")
          || uri.startsWith("/actuator")
          || uri.startsWith("/h2-console")
          || uri.startsWith("/swagger-ui")
          || uri.startsWith("/static")
          || uri.startsWith("/externals")
          || uri.startsWith("/batch")
          || uri.startsWith("/resource")) {
        filterChain.doFilter(request, response);
        return;
      }

      String authHeader = request.getHeader("Authorization");
      if (authHeader == null) {
        throw new TokenNotFoundException();
      }

      String token = authHeader.substring("Bearer ".length());
      String id = tokenGenerator.getId(token);
      User user = userRepository.findUserByuId(id).orElseThrow();
      if (!PROMOTION_REGISTER_URI.equals(uri) && !user.hasActivePromotion()) {
        response.setStatus(499);
      } else {
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
      }

    } catch (ExpiredTokenException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
    } catch (Exception e) {
      response.setStatus(HttpStatus.FORBIDDEN.value());
    }
  }
}
