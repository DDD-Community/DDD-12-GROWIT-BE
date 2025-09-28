package com.growit.app.advice.controller;

import static com.epages.restdocs.apispec.SimpleType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.growit.app.common.docs.FieldBuilder;
import org.springframework.restdocs.payload.FieldDescriptor;

public class AdviceDocumentFields {
  public static final String TAG = "Advice";
  
  public static final FieldDescriptor[] GRORONG_ADVICE_RESPONSE_FIELDS = {
    fieldWithPath("data.saying").type(STRING).description("그로롱이 전하는 명언"),
    fieldWithPath("data.message").type(STRING).description("현재 기분에 따른 격려 메시지"),
    fieldWithPath("data.mood").type(STRING).description("현재 기분 상태 (HAPPY, NORMAL, SAD)")
  };
  
  public static final FieldDescriptor[] MENTOR_ADVICE_RESPONSE_FIELDS = 
    FieldBuilder.create()
      .addFields(
        fieldWithPath("data.isChecked").type(BOOLEAN).description("조언 확인 여부"),
        fieldWithPath("data.message").type(STRING).description("멘토 조언 메시지"),
        fieldWithPath("data.kpt").description("KPT 피드백 객체"),
        fieldWithPath("data.kpt.keep").type(STRING).description("Keep - 계속할 것"),
        fieldWithPath("data.kpt.problem").type(STRING).description("Problem - 문제점"),
        fieldWithPath("data.kpt.tryNext").type(STRING).description("Try - 시도할 것")
      )
      .build();
}