package com.growit.app.resource.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationResponse {
  private String message;

  public static InvitationResponse of(String message) {
    return InvitationResponse.builder().message(message).build();
  }
}
