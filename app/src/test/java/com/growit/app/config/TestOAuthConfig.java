package com.growit.app.config;

import com.growit.app.common.config.oauth.KakaoOAuth2UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@TestConfiguration
public class TestOAuthConfig {

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private KakaoOAuth2UserService kakaoOAuth2UserService;
}
