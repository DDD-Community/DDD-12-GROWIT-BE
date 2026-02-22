package com.growit.app.user.usecase;

import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.domain.token.UserTokenRepository;
import com.growit.app.user.domain.token.service.UserTokenQuery;
import com.growit.app.user.domain.user.AppleTokenRevocationPort;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.OAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteUserUseCase {
  private final UserTokenQuery userTokenQuery;
  private final UserTokenRepository userTokenRepository;
  private final UserRepository userRepository;
  private final AppleTokenRevocationPort appleTokenRevocationPort;

  @Transactional
  public void execute(User user) {
    revokeAppleTokenIfPresent(user);
    final UserToken userToken = userTokenQuery.getUserTokenByUserId(user.getId());
    userTokenRepository.deleteUserToken(userToken);
    user.deleted();
    userRepository.saveUser(user);
  }

  private void revokeAppleTokenIfPresent(User user) {
    if (!user.hasProvider(OAuth.APPLE)) return;

    user.getOauthAccounts().stream()
        .filter(oauth -> OAuth.APPLE.equals(oauth.provider()))
        .findFirst()
        .ifPresent(
            oauth -> {
              if (oauth.refreshToken() != null) {
                log.info("Revoking Apple OAuth token for user: {}", user.getId());
                appleTokenRevocationPort.revoke(oauth.refreshToken());
              }
            });
  }
}
