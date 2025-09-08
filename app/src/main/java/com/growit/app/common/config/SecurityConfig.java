package com.growit.app.common.config;

import com.growit.app.common.config.jwt.JwtFilter;
import com.growit.app.common.config.oauth.KakaoOAuth2UserService;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
  private final TokenGenerator tokenGenerator;
  private final UserRepository userRepository;

  private final KakaoOAuth2UserService kakaoOAuth2UserService;


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .requestCache(RequestCacheConfigurer::disable)
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/actuator/**",
                        "/oauth2/**",
                        "/auth/**",
                        "/h2-console/**",
                        "/resource/**",
                        "/docs/**",
                        "/static/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
      .oauth2Login(oauth -> oauth
        .loginPage("/login")  // 커스텀 로그인 페이지가 없으면 생략 가능
        .userInfoEndpoint(u -> u.userService(kakaoOAuth2UserService))
      )
        .addFilterBefore(new JwtFilter(tokenGenerator, userRepository), AuthorizationFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
