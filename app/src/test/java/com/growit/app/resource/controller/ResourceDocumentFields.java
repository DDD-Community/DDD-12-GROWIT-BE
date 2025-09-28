package com.growit.app.resource.controller;

import static com.epages.restdocs.apispec.SimpleType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;

public class ResourceDocumentFields {
  public static final String JOB_ROLE_TAG = "JobRole";
  public static final String SAYING_TAG = "Saying";
  public static final String INVITATION_TAG = "External";
  
  public static final FieldDescriptor[] JOB_ROLE_RESPONSE_FIELDS = {
    fieldWithPath("data.jobRoles[].id").type(STRING).description("직무 ID"),
    fieldWithPath("data.jobRoles[].name").type(STRING).description("직무 이름")
  };
  
  
  public static final FieldDescriptor[] INVITATION_REQUEST_FIELDS = {
    fieldWithPath("phone").type(STRING).description("전화번호 (010-1234-5678 형식)")
  };
  
  public static final FieldDescriptor[] INVITATION_RESPONSE_FIELDS = {
    fieldWithPath("data.message").type(STRING).description("응답 메시지")
  };
}