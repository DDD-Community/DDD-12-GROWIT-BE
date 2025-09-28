package com.growit.app.goal.controller;

import static com.epages.restdocs.apispec.SimpleType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class GoalDocumentFields {
  public static final String TAG = "Goal";
  
  public static final FieldDescriptor[] CREATE_GOAL_REQUEST_FIELDS = {
    fieldWithPath("name").type(JsonFieldType.STRING).description("목표 이름"),
    fieldWithPath("duration.startDate").type(JsonFieldType.STRING).description("시작일 (yyyy-MM-dd)"),
    fieldWithPath("duration.endDate").type(JsonFieldType.STRING).description("종료일 (yyyy-MM-dd)"),
    fieldWithPath("toBe").type(JsonFieldType.STRING).description("목표 달성 후 상태"),
    fieldWithPath("category").type(JsonFieldType.STRING).description("목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"),
    fieldWithPath("plans[].weekOfMonth").type(JsonFieldType.NUMBER).description("계획 주차"),
    fieldWithPath("plans[].content").type(JsonFieldType.STRING).description("계획 내용")
  };
  
  public static final FieldDescriptor[] GOAL_RESPONSE_FIELDS = {
    fieldWithPath("data.id").type(STRING).description("목표 ID"),
    fieldWithPath("data.name").type(STRING).description("목표 이름"),
    fieldWithPath("data.duration").description("기간 정보 객체"),
    fieldWithPath("data.duration.startDate").type(STRING).description("시작일 (yyyy-MM-dd)"),
    fieldWithPath("data.duration.endDate").type(STRING).description("종료일 (yyyy-MM-dd)"),
    fieldWithPath("data.toBe").type(STRING).description("목표 달성 후 상태"),
    fieldWithPath("data.category").type(STRING).description("목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"),
    fieldWithPath("data.mentor").type(STRING).description("멘토"),
    fieldWithPath("data.plans").description("계획 리스트"),
    fieldWithPath("data.plans[].id").type(STRING).description("계획 ID"),
    fieldWithPath("data.plans[].weekOfMonth").type(NUMBER).description("주차"),
    fieldWithPath("data.plans[].duration").description("기간 정보 객체"),
    fieldWithPath("data.plans[].duration.startDate").type(STRING).description("시작일 (yyyy-MM-dd)"),
    fieldWithPath("data.plans[].duration.endDate").type(STRING).description("종료일 (yyyy-MM-dd)"),
    fieldWithPath("data.plans[].content").type(STRING).description("계획 내용")
  };
  
  public static final FieldDescriptor[] GOALS_LIST_RESPONSE_FIELDS = {
    fieldWithPath("data[].id").type(STRING).description("목표 ID"),
    fieldWithPath("data[].name").type(STRING).description("목표 이름"),
    fieldWithPath("data[].duration").description("기간 정보 객체"),
    fieldWithPath("data[].duration.startDate").type(STRING).description("시작일 (yyyy-MM-dd)"),
    fieldWithPath("data[].duration.endDate").type(STRING).description("종료일 (yyyy-MM-dd)"),
    fieldWithPath("data[].toBe").type(STRING).description("목표 달성 후 상태"),
    fieldWithPath("data[].category").type(STRING).description("목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"),
    fieldWithPath("data[].mentor").type(STRING).description("멘토"),
    fieldWithPath("data[].plans").description("계획 리스트"),
    fieldWithPath("data[].plans[].id").type(STRING).description("계획 ID"),
    fieldWithPath("data[].plans[].weekOfMonth").type(NUMBER).description("주차"),
    fieldWithPath("data[].plans[].duration").description("기간 정보 객체"),
    fieldWithPath("data[].plans[].duration.startDate").type(STRING).description("시작일 (yyyy-MM-dd)"),
    fieldWithPath("data[].plans[].duration.endDate").type(STRING).description("종료일 (yyyy-MM-dd)"),
    fieldWithPath("data[].plans[].content").type(STRING).description("계획 내용")
  };
  
}