package com.growit.app.user.controller.dto.response;

import com.growit.app.resource.domain.jobrole.JobRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
  private String id;
  private String email;
  private String name;
  private JobRole jobRole;
  private String careerYear;
}
