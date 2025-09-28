package com.growit.app.retrospect.controller;

import static com.epages.restdocs.apispec.SimpleType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.growit.app.common.docs.FieldBuilder;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class RetrospectDocumentFields {
  public static final String TAG = "Retrospect";
  public static final String GOAL_RETROSPECT_TAG = "Goal Retrospect";
  
  public static final FieldDescriptor[] CREATE_RETROSPECT_REQUEST_FIELDS = 
    FieldBuilder.create()
      .addFields(
        fieldWithPath("goalId").type(JsonFieldType.STRING).description("목표 아이디"),
        fieldWithPath("planId").type(JsonFieldType.STRING).description("계획 아이디"),
        fieldWithPath("content").type(JsonFieldType.STRING).description("회고 내용(v1)"),
        fieldWithPath("kpt").description("KPT 회고"),
        fieldWithPath("kpt.keep").type(STRING).description("Keep - 계속 유지할 것"),
        fieldWithPath("kpt.problem").type(STRING).description("Problem - 문제점"),
        fieldWithPath("kpt.tryNext").type(STRING).description("Try - 다음에 시도해볼 것")
      )
      .build();
  
  public static final FieldDescriptor[] UPDATE_RETROSPECT_REQUEST_FIELDS = 
    FieldBuilder.create()
      .addFields(
        fieldWithPath("content").type(JsonFieldType.STRING).description("회고 내용(v1)"),
        fieldWithPath("kpt").description("KPT 회고"),
        fieldWithPath("kpt.keep").type(STRING).description("Keep - 계속 유지할 것"),
        fieldWithPath("kpt.problem").type(STRING).description("Problem - 문제점"),
        fieldWithPath("kpt.tryNext").type(STRING).description("Try - 다음에 시도해볼 것")
      )
      .build();
  
  
  public static final FieldDescriptor[] CREATE_RETROSPECT_RESPONSE_FIELDS = {
    fieldWithPath("data.id").type(STRING).description("회고 ID")
  };
  
  public static final FieldDescriptor[] GET_RETROSPECT_RESPONSE_FIELDS = 
    FieldBuilder.create()
      .addFields(
        fieldWithPath("data").description("회고 목록"),
        fieldWithPath("data.retrospect").description("회고"),
        fieldWithPath("data.retrospect.id").type(STRING).description("회고 ID"),
        fieldWithPath("data.retrospect.kpt").description("KPT 회고"),
        fieldWithPath("data.retrospect.kpt.keep").type(STRING).description("Keep - 계속 유지할 것"),
        fieldWithPath("data.retrospect.kpt.problem").type(STRING).description("Problem - 문제점"),
        fieldWithPath("data.retrospect.kpt.tryNext").type(STRING).description("Try - 다음에 시도해볼 것"),
        fieldWithPath("data.plan").description("계획"),
        fieldWithPath("data.plan.id").type(STRING).description("계획 ID"),
        fieldWithPath("data.plan.weekOfMonth").type(STRING).description("계획 주차"),
        fieldWithPath("data.plan.isCurrentWeek").type(BOOLEAN).description("현재 주차 여부"),
        fieldWithPath("data.plan.content").type(STRING).description("계획 내용")
      )
      .build();
  
  public static final FieldDescriptor[] GET_RETROSPECTS_BY_FILTER_RESPONSE_FIELDS = 
    FieldBuilder.create()
      .addFields(
        fieldWithPath("data[]").description("회고 목록"),
        fieldWithPath("data[].retrospect").description("회고"),
        fieldWithPath("data[].retrospect.id").type(STRING).description("회고 ID"),
        fieldWithPath("data[].retrospect.kpt").description("KPT 회고"),
        fieldWithPath("data[].retrospect.kpt.keep").type(STRING).description("Keep - 계속 유지할 것"),
        fieldWithPath("data[].retrospect.kpt.problem").type(STRING).description("Problem - 문제점"),
        fieldWithPath("data[].retrospect.kpt.tryNext").type(STRING).description("Try - 다음에 시도해볼 것"),
        fieldWithPath("data[].plan").description("계획"),
        fieldWithPath("data[].plan.id").type(STRING).description("계획 ID"),
        fieldWithPath("data[].plan.weekOfMonth").type(STRING).description("계획 주차"),
        fieldWithPath("data[].plan.isCurrentWeek").type(BOOLEAN).description("현재 주차 여부"),
        fieldWithPath("data[].plan.content").type(STRING).description("계획 내용")
      )
      .build();
  
  public static final FieldDescriptor[] CHECK_RETROSPECT_RESPONSE_FIELDS = {
    fieldWithPath("data.isExist").description("회고 존재 여부 (true/false)")
  };
  
  public static final FieldDescriptor[] GOAL_RETROSPECT_GET_RESPONSE_FIELDS = {
    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
    fieldWithPath("data.id").type(JsonFieldType.STRING).description("목표회고 ID"),
    fieldWithPath("data.goalId").type(JsonFieldType.STRING).description("목표 ID"),
    fieldWithPath("data.todoCompletedRate").type(JsonFieldType.NUMBER).description("할일 완료율"),
    fieldWithPath("data.analysis").type(JsonFieldType.OBJECT).description("분석 결과"),
    fieldWithPath("data.analysis.summary").type(JsonFieldType.STRING).description("요약"),
    fieldWithPath("data.analysis.advice").type(JsonFieldType.STRING).description("조언"),
    fieldWithPath("data.content").type(JsonFieldType.STRING).description("회고 내용")
  };
  
  public static final FieldDescriptor[] GOAL_RETROSPECT_CREATE_REQUEST_FIELDS = {
    fieldWithPath("goalId").type(JsonFieldType.STRING).description("목표 아이디")
  };
  
  public static final FieldDescriptor[] GOAL_RETROSPECT_CREATE_RESPONSE_FIELDS = {
    fieldWithPath("data.id").type(JsonFieldType.STRING).description("목표 회고 아이디")
  };
  
  public static final FieldDescriptor[] GOAL_RETROSPECT_UPDATE_REQUEST_FIELDS = {
    fieldWithPath("content").type(JsonFieldType.STRING).description("회고 내용")
  };
  
  public static final FieldDescriptor[] GOAL_RETROSPECTS_BY_YEAR_RESPONSE_FIELDS = {
    fieldWithPath("data").type(JsonFieldType.ARRAY).description("목록 데이터 배열"),
    fieldWithPath("data[].goal").type(JsonFieldType.OBJECT).description("목표 정보"),
    fieldWithPath("data[].goal.id").type(JsonFieldType.STRING).description("목표 ID"),
    fieldWithPath("data[].goal.name").type(JsonFieldType.STRING).description("목표명"),
    fieldWithPath("data[].goal.duration").type(JsonFieldType.OBJECT).description("목표 기간"),
    fieldWithPath("data[].goal.duration.startDate").type(JsonFieldType.STRING).description("시작일 (yyyy-MM-dd)"),
    fieldWithPath("data[].goal.duration.endDate").type(JsonFieldType.STRING).description("종료일 (yyyy-MM-dd)"),
    fieldWithPath("data[].goalRetrospect").type(JsonFieldType.OBJECT).optional().description("목표 회고 정보 (null 가능)"),
    fieldWithPath("data[].goalRetrospect.id").type(JsonFieldType.STRING).optional().description("목표 회고 ID"),
    fieldWithPath("data[].goalRetrospect.isCompleted").type(JsonFieldType.BOOLEAN).optional().description("회고 작성 완료 여부 (내용 미작성 포함)")
  };
}