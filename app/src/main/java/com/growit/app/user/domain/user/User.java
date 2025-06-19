package com.growit.app.user.domain.user;

import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class User {
  private String id;

  private Email email;

  private String password;

  private String name;

  private String jobRoleId;

  private CareerYear careerYear;
}
