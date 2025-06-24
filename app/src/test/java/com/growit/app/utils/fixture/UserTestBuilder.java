package com.growit.app.utils.fixture;

import com.growit.app.user.domain.user.User;
import com.growit.app.user.domain.user.vo.CareerYear;
import com.growit.app.user.domain.user.vo.Email;
import com.growit.app.user.domain.user.vo.RequiredConsent;

public class UserTestBuilder {
  private String id = "user-1";
  private Email email = new Email("test@example.com");
  private String password = "pw1234";
  private String name = "홍길동";
  private String jobRoleId = "dev";
  private CareerYear careerYear = CareerYear.NEWBIE;
  private RequiredConsent requiredConsent = requiredConsent().build().getRequiredConsent();

  public UserTestBuilder id(String id) { this.id = id; return this; }
  public UserTestBuilder email(Email email) { this.email = email; return this; }
  public UserTestBuilder password(String password) { this.password = password; return this; }
  public UserTestBuilder name(String name) { this.name = name; return this; }
  public UserTestBuilder jobRoleId(String jobRoleId) { this.jobRoleId = jobRoleId; return this; }
  public UserTestBuilder careerYear(CareerYear careerYear) { this.careerYear = careerYear; return this; }
  public UserTestBuilder requiredConsent(RequiredConsent requiredConsent) { this.requiredConsent = requiredConsent; return this; }

  public User build() {
    return new User(id, email, password, name, jobRoleId, careerYear, requiredConsent);
  }
}
