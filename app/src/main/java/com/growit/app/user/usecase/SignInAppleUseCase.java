package com.growit.app.user.usecase;

import com.growit.app.common.config.oauth.AppleKeys;
import com.growit.app.user.controller.dto.response.OAuthResponse;
import com.growit.app.user.controller.dto.response.TokenResponse;
import com.growit.app.user.domain.token.service.AppleIdTokenValidator;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.service.UserTokenSaver;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.dto.OAuthCommand;
import com.growit.app.user.domain.user.dto.SignInAppleCommand;
import com.growit.app.user.infrastructure.client.AppleAuthClient;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInAppleUseCase {

    private final AppleIdTokenValidator appleIdTokenValidator;
    private final AppleAuthClient appleAuthClient;
    private final OAuthLinkUseCase oAuthLinkUseCase;
    private final TokenGenerator tokenGenerator;
    private final UserTokenSaver userTokenSaver;
    private final TokenService tokenService;

    @Transactional
    public SignInAppleResult execute(SignInAppleCommand command) {
        // 1. Verify id_token and extract attributes
        Map<String, Object> attributes = appleIdTokenValidator.parseAndVerifyIdToken(command.idToken());
        String sub = (String) attributes.get(AppleKeys.SUB);
        String email = (String) attributes.get(AppleKeys.EMAIL);

        // 2. Exchange authorization_code for refresh_token (for revocation support)
        String refreshToken = appleAuthClient.getRefreshToken(command.authorizationCode());

        // 3. Find existing user or link new oauth entry
        OAuthCommand oauthCommand = new OAuthCommand(email, AppleKeys.PROVIDER_NAME, sub, refreshToken);
        Optional<User> existingUser = oAuthLinkUseCase.execute(oauthCommand);

        if (existingUser.isPresent()) {
            // Existing member -> Login
            User user = existingUser.get();
            Token token = tokenGenerator.createToken(user);
            userTokenSaver.saveUserToken(user.getId(), token);
            
            TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(token.accessToken())
                .refreshToken(token.refreshToken())
                .build();
                
            return new SignInAppleResult(false, tokenResponse, null);
        } else {
            // New user -> Pending Signup -> Issue registration token
            String regToken = tokenService.createRegistrationToken(
                AppleKeys.PROVIDER_NAME, sub, email, refreshToken
            );
            
            OAuthResponse oauthResponse = OAuthResponse.builder()
                .registrationToken(regToken)
                // Apple only provides name on first auth, but we don't have it in id_token
                .name(null) 
                .build();
                
            return new SignInAppleResult(true, null, oauthResponse);
        }
    }
}
