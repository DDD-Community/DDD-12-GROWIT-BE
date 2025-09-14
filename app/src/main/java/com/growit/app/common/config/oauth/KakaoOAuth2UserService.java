package com.growit.app.common.config.oauth;

import com.growit.app.user.domain.token.service.JwtClaimKeys;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.OAuthCommand;
import com.growit.app.user.usecase.OAuthLinkUseCase;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final OAuthLinkUseCase oAuthLinkUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(req);

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        KakaoProfile profile = KakaoProfile.from(attributes);

        Optional<User> existing = oAuthLinkUseCase.execute(new OAuthCommand(
          profile.getEmail(),
          profile.getProvider(),
          profile.getProviderId()
        ));

        if (existing.isPresent()) {
            User user = existing.get();
            attributes.put(JwtClaimKeys.USER, user);
            return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("USER")),
                attributes,
                JwtClaimKeys.ID
            );
        } else {
            attributes.put(JwtClaimKeys.PENDING_SIGNUP, true);
            attributes.put(JwtClaimKeys.PROVIDER, KakaoKeys.PROVIDER_NAME);
            attributes.put(JwtClaimKeys.PROVIDER_ID, profile.getProviderId());
            attributes.put(JwtClaimKeys.EMAIL, profile.getEmail());
            attributes.put(JwtClaimKeys.NICK_NAME, profile.getNickname());

          return new DefaultOAuth2User(
                List.of(),
                attributes,
                JwtClaimKeys.ID
            );
        }
    }

    @Getter
    @Builder
    public static class KakaoProfile {
        private String provider;  // "kakao"
        private String providerId; // id
        private String email;
        private String nickname;
        private String profileImage;

        @SuppressWarnings("unchecked")
        public static KakaoProfile from(Map<String, Object> attrs) {
            Map<String, Object> account = (Map<String, Object>) attrs.getOrDefault(KakaoKeys.KAKAO_ACCOUNT, Map.of());
            Map<String, Object> profile = (Map<String, Object>) account.getOrDefault(KakaoKeys.PROFILE, Map.of());

            return KakaoProfile.builder()
                .provider(KakaoKeys.PROVIDER_NAME)
                .providerId(String.valueOf(attrs.get(KakaoKeys.ID)))
                .email((String) account.get(KakaoKeys.EMAIL))
                .nickname((String) profile.get(KakaoKeys.NICKNAME))
                .profileImage((String) profile.get(KakaoKeys.PROFILE_IMAGE_URL))
                .build();
        }
    }
}
