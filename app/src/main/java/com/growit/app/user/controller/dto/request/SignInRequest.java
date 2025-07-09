package com.growit.app.user.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {

  @NotBlank(message = "{validation.signin.email.required}")
  @Email(message = "{validation.signin.email.invalid}")
  private String email;

  @NotBlank(message = "{validation.signin.password.required}")
  @Size(min = 8, message = "{validation.signin.password.size}")
  private String password;
}
