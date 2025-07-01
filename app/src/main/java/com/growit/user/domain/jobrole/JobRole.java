package com.growit.user.domain.jobrole;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobRole {
  private String id;
  private String name;
}
