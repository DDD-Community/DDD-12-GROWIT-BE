package com.growit.app.common.config;

import com.growit.app.common.config.jwt.JwtFilter;
import com.growit.app.common.config.oauth.CustomOAuth2AuthorizationRequestResolver;
import com.growit.app.common.config.oauth.KakaoOAuth2UserService;
import com.growit.app.common.config.oauth.OAuth2LoginFailureHandler;
import com.growit.app.common.config.oauth.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtFilter jwtFilter;
  private final KakaoOAuth2UserService kakaoOAuth2UserService;

  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  private final CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Profile("!test")
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
                        "/test/**",
                        "/actuator/**",
                        "/login/**",
                        "/oauth2/**",
                        "/auth/**",
                        "/h2-console/**",
                        "/resource/**",
                        "/externals/**",
                        "/docs/**",
                        "/batch/**",
                        "/static/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth ->
                oauth
                    .authorizationEndpoint(
                        authorization ->
                            authorization.authorizationRequestResolver(
                                customOAuth2AuthorizationRequestResolver))
                    .userInfoEndpoint(u -> u.userService(kakaoOAuth2UserService))
                    .successHandler(oAuth2LoginSuccessHandler)
                    .failureHandler(oAuth2LoginFailureHandler))
        .addFilterBefore(jwtFilter, AuthorizationFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
    return http.build();
  }
}
