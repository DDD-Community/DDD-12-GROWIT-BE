package com.growit.app.user.domain.entity;

import com.growit.app.user.domain.vo.Email;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID")
  private UUID id;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "email", length = 50, nullable = false, unique = true))
  private Email email;

  @Column(length = 64, nullable = false)
  private String password;

  @Column(length = 20, nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "jobRole")
  private JobRoleEntity jobRole;

  @Enumerated(EnumType.STRING)
  @Column(name = "careerYear", length = 32, nullable = false)
  private CareerYear careerYear;

  @Column(length = 32)
  private String refreshToken;

  @Builder
  public UserEntity(Email email, String password, String name, JobRoleEntity jobRole, CareerYear careerYear, String refreshToken) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.jobRole = jobRole;
    this.careerYear = careerYear;
    this.refreshToken = refreshToken;
  }
}
