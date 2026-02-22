package com.growit.app.user.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.message.ErrorCode;
import com.growit.app.user.domain.user.AppleTokenRevocationPort;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.vo.OAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevokeAppleAccountUseCase {

  private final UserRepository userRepository;
  private final AppleTokenRevocationPort appleTokenRevocationPort;

  @Transactional
  public void execute(String userId) {
    User user =
        userRepository
            .findUserByuId(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getCode()));

    if (user.getOauthAccounts() == null) return;

    user.getOauthAccounts().stream()
        .filter(oauth -> OAuth.APPLE.equals(oauth.provider()))
        .findFirst()
        .ifPresent(
            oauth -> {
              String refreshToken = oauth.refreshToken();
              if (refreshToken != null) {
                log.info("Revoking Apple OAuth for user: {}", userId);
                appleTokenRevocationPort.revoke(refreshToken);
              } else {
                log.warn("Apple Refresh Token not found for user: {}", userId);
              }
            });
  }
}
