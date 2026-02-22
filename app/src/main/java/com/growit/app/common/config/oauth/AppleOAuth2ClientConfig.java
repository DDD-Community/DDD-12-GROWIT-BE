package com.growit.app.common.config.oauth;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class AppleOAuth2ClientConfig {

  private final AppleClientSecretGenerator appleClientSecretGenerator;

  @Bean
  public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
      accessTokenResponseClient() {
    DefaultAuthorizationCodeTokenResponseClient client =
        new DefaultAuthorizationCodeTokenResponseClient();

    client.setRequestEntityConverter(new AppleOAuth2AuthorizationCodeGrantRequestEntityConverter());

    RestTemplate restTemplate =
        new RestTemplate(
            Arrays.asList(
                new FormHttpMessageConverter(),
                new OAuth2AccessTokenResponseHttpMessageConverter()));
    restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
    client.setRestOperations(restTemplate);

    return client;
  }

  private class AppleOAuth2AuthorizationCodeGrantRequestEntityConverter
      extends OAuth2AuthorizationCodeGrantRequestEntityConverter {

    @Override
    protected MultiValueMap<String, String> createParameters(
        OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
      MultiValueMap<String, String> parameters =
          super.createParameters(authorizationCodeGrantRequest);

      if (AppleKeys.PROVIDER_NAME.equals(
          authorizationCodeGrantRequest
              .getClientRegistration()
              .getRegistrationId()
              .toLowerCase())) {
        parameters.set("client_secret", appleClientSecretGenerator.createClientSecret());
      }

      return parameters;
    }
  }
}
