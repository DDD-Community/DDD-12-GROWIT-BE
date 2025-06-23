package com.growit.app.goal.domain.goal.service;

import com.growit.app.common.exception.BaseException;

public class GoalNotFoundException extends BaseException {
  public GoalNotFoundException() {
    super("해당 되는 목표가 존재하지 않습니다.");
  }
}
