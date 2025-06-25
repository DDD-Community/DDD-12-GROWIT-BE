package com.growit.app.utils.fixture;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.RequiredConsent;

public class UserTestBuilder {
  private String id = "user-1";
  private String email = "test@example.com";
  private String password = "password123";
  private String name = "홍길동";
  private String jobRoleId = "dev";
  private CareerYear careerYear = CareerYear.NEWBIE;
  private RequiredConsent requiredConsent = new RequiredConsent(true, true);

  private UserTestBuilder() {}

  public static UserTestBuilder aUser() {
    return new UserTestBuilder();
  }

  public UserTestBuilder withId(String id) {
    this.id = id;
    return this;
  }

  public UserTestBuilder withEmail(String email) {
    this.email = email;
    return this;
  }

  public UserTestBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public UserTestBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public UserTestBuilder withJobRoleId(String jobRoleId) {
    this.jobRoleId = jobRoleId;
    return this;
  }

  public UserTestBuilder withCareerYear(CareerYear careerYear) {
    this.careerYear = careerYear;
    return this;
  }

  public UserTestBuilder withRequiredConsent(RequiredConsent consent) {
    this.requiredConsent = consent;
    return this;
  }

  public User build() {
    return new User(id, new Email(email), password, name, jobRoleId, careerYear, requiredConsent);
  }
}
