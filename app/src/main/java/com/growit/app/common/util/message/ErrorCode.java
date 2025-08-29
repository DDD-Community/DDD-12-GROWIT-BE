package com.growit.app.common.util.message;

import lombok.Getter;

// DOMAIN_ERROR 형태로 네이밍을 가져갑니다.
// error.domain.description (- 를 활용)
@Getter
public enum ErrorCode {
  GOAL_ALREADY_EXISTS("error.goal.already-exists"),
  GOAL_NOT_FOUND("error.goal.not-found"),
  GOAL_NOT_EXISTS_DATE("error.goal.not-exists-date"),
  GOAL_PLAN_NOT_FOUND("error.goal.plan-not-found"),
  GOAL_PLAN_COUNT_NOT_MATCHED(
      "error.goal.plan-count-not-matched"), // "설정한 날짜 범위와 주간 계획수가 일치하지 않습니다."
  GOAL_PLAN_DUPLICATE("error.goal.plan-duplicate"), // "중복 되는 주차가 존재 합니다."
  GOAL_DURATION_MONDAY("error.goal.duration-monday"), // "목표 시작일은 월요일 이여야 합니다."
  GOAL_DURATION_SUNDAY("error.goal.duration-sunday"), // "목표 종료일은 일요일 이여야 합니다."
  GOAL_DURATION_START_END("error.goal.duration-start-end"), // "목표 종료일은 시작일보다 뒤여야 합니다."
  GOAL_DURATION_START_AFTER_TODAY(
      "error.goal.duration-start-after-today"), // "목표 시작일은 오늘 이후부터 가능합니다."
  GOAL_PROGRESS_NOTFOUND("error.goal.progress-not-found"), // 진행중인 목표를 찾을 수 없습니다.
  GOAL_ENDED_DO_NOT_CHANGE("error.goal.ended-goal-do-not-change"),
  GOAL_PARTIALLY_DO_NOT_CHANGE_DURATION("error.goal.partially-goal-do-not-change-duration"),
  USER_NOT_FOUND("error.user.not-found"),
  USER_INVALID_EMAIL("error.user.invalid-email"), // "유효하지 않은 이메일입니다."
  USER_ALREADY_REGISTERED("error.user.already-registered"), // ""해당 이메일로 이미 가입된 계정이 있습니다.""
  USER_TOKEN_EXPIRED("error.user.token-expired"), // "토큰정보가 만료되었습니다."
  USER_TOKEN_INVALID("error.user.token-invalid"), // "토큰정보가 올바르지 않습니다."
  USER_TOKEN_NOT_FOUND("error.user.token-not-found"), // "토큰정보가 존재하지 않습니다."
  RESOURCE_JOBROLE_NOT_FOUND("error.resource.job-role-not-found"), // "직무가 존재하지 않습니다"
  USER_REQUIRED_INVALID("error.user.required-invalid"), // "필수 약관 동의 필요"
  USER_SIGN_IN_FAILED("error.user.sign-in-failed"), // "로그인 정보를 확인해주세요"
  RETROSPECT_ALREADY_EXISTS_BY_PLAN(
      "error.retrospect-already-exists-by-plan"), // "해당 주간 계획에 대한 회고가 이미 존재합니다."
  TODO_IS_EXIST("error.todo.is-exist"), // "ToDo가 존재합니다."
  GOAL_RETROSPECT_GOAL_NOT_COMPLETED("error.goal-retrospect-not-completed"), // "목표가 완료되지 않았습니다."
  GOAL_RETROSPECT_ALREADY_EXISTS("error.goal-retrospect-already_exists"), // "이미 회고가 존재합니다"
  RETROSPECT_NOT_FOUND("error.retrospect-not-found"); // "회고 정보가 존재하지 않습니다."
  private final String code;

  ErrorCode(String code) {
    this.code = code;
  }
}
