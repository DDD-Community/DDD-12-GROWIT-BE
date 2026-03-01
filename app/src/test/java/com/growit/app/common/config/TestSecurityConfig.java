package com.growit.app.common.config;

import com.growit.app.advice.infrastructure.client.AiMentorAdviceClientImpl;
import com.growit.app.common.config.jwt.JwtFilter;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@TestConfiguration
public class TestSecurityConfig {

  @MockitoBean private JwtFilter jwtFilter;

  @MockitoBean private TokenGenerator tokenGenerator;

  @MockitoBean private TokenService tokenService;

  @MockitoBean private UserRepository userRepository;

  @MockitoBean private MessageService messageService;

  @MockitoBean private AiMentorAdviceClientImpl aiMentorAdviceClient;

  @Bean
  @Primary
  public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .oauth2Login(AbstractHttpConfigurer::disable)
        .oauth2Client(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
