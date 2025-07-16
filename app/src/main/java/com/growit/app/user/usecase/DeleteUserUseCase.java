package com.growit.app.user.usecase;

import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.domain.token.UserTokenRepository;
import com.growit.app.user.domain.token.service.UserTokenQuery;
import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteUserUseCase {
  private final UserTokenQuery userTokenQuery;
  private final UserTokenRepository userTokenRepository;
  private final UserRepository userRepository;

  @Transactional
  public void execute(User user) {
    final UserToken userToken = userTokenQuery.getUserTokenByUserId(user.getId());
    userTokenRepository.deleteUserToken(userToken);
    user.deleted();
    userRepository.saveUser(user);
  }
}
