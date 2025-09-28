package com.growit.app.common.docs;

import static com.epages.restdocs.apispec.SimpleType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;

public class CommonFields {
  
  public static final FieldDescriptor[] STANDARD_RESPONSE = {
    fieldWithPath("data").description("응답 데이터")
  };
  
  public static final FieldDescriptor[] SUCCESS_MESSAGE_RESPONSE = {
    fieldWithPath("data").type(STRING).description("성공 메시지")
  };
  
  public static final FieldDescriptor[] CONSENT_FIELDS = {
    fieldWithPath("requiredConsent.privacyPolicyAgreed")
        .type(BOOLEAN)
        .description("개인정보 동의"),
    fieldWithPath("requiredConsent.serviceTermsAgreed")
        .type(BOOLEAN)
        .description("서비스 약관 동의")
  };
  
  public static final FieldDescriptor[] TOKEN_RESPONSE = {
    fieldWithPath("data.accessToken").type(STRING).description("액세스 토큰"),
    fieldWithPath("data.refreshToken").type(STRING).description("리프레시 토큰")
  };
}