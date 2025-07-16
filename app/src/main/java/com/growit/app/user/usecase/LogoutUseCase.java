package com.growit.app.user.usecase;

import com.growit.app.common.exception.BaseException;
import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.domain.token.UserTokenRepository;
import com.growit.app.user.domain.token.service.UserTokenQuery;
import com.growit.app.user.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogoutUseCase {
  private final UserTokenRepository userTokenRepository;
  private final UserTokenQuery userTokenQuery;

  @Transactional
  public void execute(User user) throws BaseException {
    final UserToken userToken = userTokenQuery.getUserTokenByUserId(user.getId());
    userTokenRepository.deleteUserToken(userToken);
  }
}
