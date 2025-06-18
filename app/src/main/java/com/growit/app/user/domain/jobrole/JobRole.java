package com.growit.app.user.domain.jobrole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class JobRole {
  private String id;
  private String name;
}
