package com.growit.app.user.usecase;

import com.growit.app.common.exception.NotFoundException;
import com.growit.app.common.util.message.ErrorCode;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateOAuthTokenUseCase {

  private final UserRepository userRepository;

  @Transactional
  public void execute(String userId, String provider, String refreshToken) {
    User user =
        userRepository
            .findUserByuId(userId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND.getCode()));

    user.updateOAuthRefreshToken(provider, refreshToken);
    userRepository.saveUser(user);
  }
}
