package com.growit.app.common.config;

import com.growit.app.common.config.jwt.JwtFilter;
import com.growit.app.common.config.oauth.KakaoOAuth2UserService;
import com.growit.app.common.config.oauth.OAuth2LoginFailureHandler;
import com.growit.app.common.config.oauth.OAuth2LoginSuccessHandler;
import com.growit.app.common.util.message.MessageService;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.user.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@TestConfiguration
public class TestSecurityConfig {

  @MockitoBean private JwtFilter jwtFilter;

  @MockitoBean private KakaoOAuth2UserService kakaoOAuth2UserService;

  @MockitoBean private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  @MockitoBean private OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

  @MockitoBean private TokenGenerator tokenGenerator;

  @MockitoBean private TokenService tokenService;

  @MockitoBean private UserRepository userRepository;

  @MockitoBean private MessageService messageService;

  @MockitoBean private com.growit.app.advice.scheduler.MentorAdviceScheduler mentorAdviceScheduler;

  @MockitoBean
  private com.growit.app.advice.infrastructure.client.AiMentorAdviceClientImpl aiMentorAdviceClient;

  @Bean
  @Primary
  @Profile("test")
  public ClientRegistrationRepository clientRegistrationRepository() {
    ClientRegistration dummyRegistration =
        ClientRegistration.withRegistrationId("kakao")
            .clientId("dummy-client-id")
            .clientSecret("dummy-client-secret")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost:8080/login/oauth2/code/kakao")
            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
            .tokenUri("https://kauth.kakao.com/oauth/token")
            .userInfoUri("https://kapi.kakao.com/v2/user/me")
            .userNameAttributeName("id")
            .build();
    return new InMemoryClientRegistrationRepository(dummyRegistration);
  }

  @Bean
  @Primary
  @Profile("test")
  public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(
      ClientRegistrationRepository clientRegistrationRepository) {
    return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
  }

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
            session ->
                session.sessionCreationPolicy(
                    org.springframework.security.config.http.SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
