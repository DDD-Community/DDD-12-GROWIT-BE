package com.growit.app.common.config;

import com.growit.app.common.config.jwt.JwtFilter;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
  private final TokenService tokenService;
  private final UserRepository userRepository;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
      .cors(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .logout(AbstractHttpConfigurer::disable)
      .requestCache(RequestCacheConfigurer::disable)
      .authorizeHttpRequests(
        auth ->
          auth.requestMatchers("/auth/**", "/actuator/**")
            .permitAll()
            .anyRequest()
            .authenticated())
      .addFilterBefore(new JwtFilter(tokenService, userRepository), AuthorizationFilter.class)
      .sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
