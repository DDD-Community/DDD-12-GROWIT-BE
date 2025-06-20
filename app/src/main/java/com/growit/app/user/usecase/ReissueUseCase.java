package com.growit.app.user.usecase;

import com.growit.app.user.domain.token.Token;
import com.growit.app.user.domain.token.service.TokenService;
import com.growit.app.user.domain.token.service.exception.InvalidTokenException;
import com.growit.app.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReissueUseCase {
  private final UserRepository userRepository;
  private final TokenService tokenService;

  public Token execute(String refreshToken) {
    final String id = tokenService.getId(refreshToken);
    userRepository.findUserByuId(id).orElseThrow(InvalidTokenException::new);

    return tokenService.reIssue(refreshToken);
  }
}
