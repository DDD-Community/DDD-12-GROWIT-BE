package com.growit.app.common.config.jwt;

import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
  private final TokenService tokenService;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String uri = httpServletRequest.getRequestURI();
      if (uri.startsWith("/auth") || uri.startsWith("/actuator")) {
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        return;
      }

      String authorizationHeader = httpServletRequest.getHeader("Authorization");
      if (authorizationHeader == null) {
        throw new Exception("Authorization header is empty");
      }

      String token = authorizationHeader.substring("Bearer ".length());
      if (tokenService.isValid(token)) {
        String id = tokenService.getId(token);
        User user = userRepository.findUserById(id).orElseThrow();
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        throw new Exception("invalid token");
      }

      filterChain.doFilter(httpServletRequest, httpServletResponse);
    } catch (ExpiredJwtException e) {
      httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    } catch (Exception e) {
      httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
    }
  }
}
