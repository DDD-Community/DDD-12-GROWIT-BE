package com.growit.app.user.domain.jobrole;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobRole {
  private String uid;
  private String name;
}
