package com.growit.app.user.controller;

import static com.epages.restdocs.apispec.SimpleType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;

public class AuthDocumentFields {
  public static final String TAG = "Auth";
  
  public static final FieldDescriptor[] SIGNUP_REQUEST_FIELDS = {
    fieldWithPath("email").type(STRING).description("사용자 이메일"),
    fieldWithPath("password").type(STRING).description("사용자 비밀번호"),
    fieldWithPath("name").type(STRING).description("사용자 이름"),
    fieldWithPath("jobRoleId").type(STRING).description("직무 ID"),
    fieldWithPath("careerYear").type(STRING).description("경력 연차 (예: JUNIOR, MID, SENIOR)")
  };
  
  public static final FieldDescriptor[] SIGNIN_REQUEST_FIELDS = {
    fieldWithPath("email").type(STRING).description("사용자 이메일"),
    fieldWithPath("password").type(STRING).description("사용자 비밀번호")
  };
  
  public static final FieldDescriptor[] REISSUE_REQUEST_FIELDS = {
    fieldWithPath("refreshToken").type(STRING).description("리프레시 토큰")
  };
  
  public static final FieldDescriptor[] SIGNUP_KAKAO_REQUEST_FIELDS = {
    fieldWithPath("name").type(STRING).description("사용자 이름"),
    fieldWithPath("jobRoleId").type(STRING).description("직무 ID"),
    fieldWithPath("careerYear").type(STRING).description("경력 연차 (예: JUNIOR, MID, SENIOR)"),
    fieldWithPath("registrationToken").type(STRING).description("카카오 등록 토큰")
  };
}