package com.growit.app.user.domain.jobrole;


import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobRole {
  private String id;
  private String name;

  public static JobRole from(String name) {
    // validation 추가
    return JobRole
      .builder()
      .id(UUID.randomUUID().toString())
      .name(name).build();
  }
}
