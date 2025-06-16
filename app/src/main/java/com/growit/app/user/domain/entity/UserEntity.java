package com.growit.app.user.domain.entity;

import com.growit.app.user.domain.vo.Email;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(length = 50, nullable = false, unique = true)
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
}
