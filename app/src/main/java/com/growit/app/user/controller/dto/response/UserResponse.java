package com.growit.app.user.controller.dto.response;

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
  private String lastName;
  private SajuInfoResponse saju;

  @Deprecated private String jobRoleId;

  @Deprecated private String careerYear;
}
