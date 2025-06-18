package com.growit.app.user.domain.jobrole;

import java.util.Base64;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobRole {
  private String id;
  private String name;

  public static JobRole from(String name) {
    // validation 추가
    return JobRole.builder()
        .id(Base64.getEncoder().encodeToString(name.getBytes()))
        .name(name)
        .build();
  }
}
