package com.growit.app.user.usecase;

import com.growit.app.user.domain.token.UserToken;
import com.growit.app.user.domain.token.UserTokenRepository;
import com.growit.app.user.domain.token.service.TokenGenerator;
import com.growit.app.user.domain.token.service.UserTokenQuery;
import com.growit.app.user.domain.token.vo.Token;
import com.growit.app.user.domain.user.dto.ReIssueCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueUseCase {
  private final UserTokenRepository userTokenRepository;
  private final UserTokenQuery userTokenQuery;
  private final TokenGenerator tokenGenerator;

  @Transactional
  public Token execute(ReIssueCommand reIssueCommand) {
    final UserToken userToken = userTokenQuery.getUserToken(reIssueCommand.refreshToken());
    final Token token = tokenGenerator.reIssue(reIssueCommand.refreshToken());

    userToken.updateToken(token.refreshToken());
    userTokenRepository.saveUserToken(userToken);

    return token;
  }
}
