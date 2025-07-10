package com.growit.app.retrospect.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RetrospectExistResponse {
  private boolean isExist;

  public boolean getIsExist() {
    return isExist;
  }
}
