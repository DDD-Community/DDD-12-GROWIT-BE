package com.growit.app.user.domain.token;

import com.growit.app.common.util.IDGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserToken {
  private String id;
  private String token;
  private String userId;

  public static UserToken from(String userId, String token) {
    return UserToken.builder().id(IDGenerator.generateId()).userId(userId).token(token).build();
  }

  public void updateToken(String token) {
    this.token = token;
  }
}
