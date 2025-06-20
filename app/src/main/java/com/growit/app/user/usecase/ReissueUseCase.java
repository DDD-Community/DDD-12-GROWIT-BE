package com.growit.app.user.usecase;

import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;
import com.growit.app.user.domain.user.UserRepository;
import com.growit.app.user.domain.user.dto.ReIssueCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueUseCase {
  private final UserRepository userRepository;
  private final TokenService tokenService;

  public Token execute(ReIssueCommand reIssueCommand) {
    final String id = tokenService.getId(reIssueCommand.refreshToken());
    userRepository.findUserByuId(id).orElseThrow(InvalidTokenException::new);

    return tokenService.reIssue(reIssueCommand.refreshToken());
  }
}
