package com.growit.app.user.domain.user;


import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;

// NOTE :: DB적 내용이 아닌, (클래스 다이어그램과 같아진 상태)
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
