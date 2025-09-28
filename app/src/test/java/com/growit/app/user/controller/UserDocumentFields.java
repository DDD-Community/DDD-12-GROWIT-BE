package com.growit.app.user.controller;

import static com.epages.restdocs.apispec.SimpleType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;

public class UserDocumentFields {
  public static final String TAG = "User";
  
  public static final FieldDescriptor[] USER_RESPONSE_FIELDS = {
    fieldWithPath("data.id").type(STRING).description("사용자 ID"),
    fieldWithPath("data.email").type(STRING).description("이메일"),
    fieldWithPath("data.name").type(STRING).description("이름"),
    fieldWithPath("data.jobRole.id").type(STRING).description("직무 ID"),
    fieldWithPath("data.jobRole.name").type(STRING).description("직무 이름"),
    fieldWithPath("data.careerYear").type(STRING).description("경력 연차")
  };
  
  public static final FieldDescriptor[] UPDATE_USER_REQUEST_FIELDS = {
    fieldWithPath("name").type(STRING).description("이름"),
    fieldWithPath("jobRoleId").type(STRING).description("직무 ID"),
    fieldWithPath("careerYear").type(STRING).description("경력 연차")
  };
  
  public static final FieldDescriptor[] ONBOARDING_RESPONSE_FIELDS = {
    fieldWithPath("data").type(BOOLEAN).description("온보딩 진행 여부")
  };
}