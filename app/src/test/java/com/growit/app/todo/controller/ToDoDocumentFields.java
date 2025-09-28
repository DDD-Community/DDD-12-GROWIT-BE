package com.growit.app.todo.controller;

import static com.epages.restdocs.apispec.SimpleType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class ToDoDocumentFields {
  public static final String TAG = "ToDo";
  
  public static final FieldDescriptor[] CREATE_TODO_REQUEST_FIELDS = {
    fieldWithPath("goalId").type(STRING).description("목표 ID"),
    fieldWithPath("date").type(STRING).description("할 일 날짜 (yyyy-MM-dd)"),
    fieldWithPath("content").type(STRING).description("할 일 내용 (5자 이상 30자 미만)")
  };
  
  public static final FieldDescriptor[] UPDATE_TODO_REQUEST_FIELDS = {
    fieldWithPath("content").type(STRING).description("수정할 할 일 내용 (5자 이상 30자 미만)"),
    fieldWithPath("date").type(STRING).description("할 일 날짜 (yyyy-MM-dd)")
  };
  
  public static final FieldDescriptor[] COMPLETED_STATUS_CHANGE_REQUEST_FIELDS = {
    fieldWithPath("isCompleted").type(BOOLEAN).description("완료 여부")
  };
  
  public static final FieldDescriptor[] FACE_STATUS_RESPONSE_FIELDS = {
    fieldWithPath("data.faceStatus").type(STRING).description("그로냥 얼굴 상태 (HAPPY, NORMAL, SAD)")
  };
  
  public static final FieldDescriptor[] CREATE_TODO_RESPONSE_FIELDS = {
    fieldWithPath("data.id").type(JsonFieldType.STRING).description("생성된 할 일 ID")
  };
  
  public static final FieldDescriptor[] GET_TODO_RESPONSE_FIELDS = {
    fieldWithPath("data").type(JsonFieldType.ARRAY).description("할 일 목록"),
    fieldWithPath("data[].id").type(JsonFieldType.STRING).description("할 일 ID"),
    fieldWithPath("data[].content").type(JsonFieldType.STRING).description("할 일 내용"),
    fieldWithPath("data[].date").type(JsonFieldType.STRING).description("할 일 날짜 (yyyy-MM-dd)"),
    fieldWithPath("data[].isCompleted").type(JsonFieldType.BOOLEAN).description("완료 여부 (true: 완료, false: 미완료)")
  };
  
  public static final FieldDescriptor[] WEEKLY_PLAN_RESPONSE_FIELDS = {
    fieldWithPath("data[]").type(JsonFieldType.OBJECT).description("계획 목록"),
    fieldWithPath("data[].id").type(JsonFieldType.STRING).description("계획 ID"),
    fieldWithPath("data[].weekOfMonth").type(JsonFieldType.NUMBER).description("주차"),
    fieldWithPath("data[].duration").type(JsonFieldType.OBJECT).description("기간 정보"),
    fieldWithPath("data[].duration.startDate").type(JsonFieldType.STRING).description("시작일 (yyyy-MM-dd)"),
    fieldWithPath("data[].duration.endDate").type(JsonFieldType.STRING).description("종료일 (yyyy-MM-dd)"),
    fieldWithPath("data[].content").type(JsonFieldType.STRING).description("계획 내용"),
    fieldWithPath("data[].isCurrentWeek").type(JsonFieldType.BOOLEAN).description("현재 주차 여부")
  };
  
  public static final FieldDescriptor[] TODAY_MISSION_RESPONSE_FIELDS = {
    fieldWithPath("data").type(JsonFieldType.ARRAY).description("미션 목록"),
    fieldWithPath("data[].id").type(JsonFieldType.STRING).description("미션 ID"),
    fieldWithPath("data[].title").type(JsonFieldType.STRING).description("미션 제목"),
    fieldWithPath("data[].description").type(JsonFieldType.STRING).description("미션 설명"),
    fieldWithPath("data[].missionType").type(JsonFieldType.STRING).description("미션 타입 (예: CHARACTER_BUILDING, GOOD_HABIT)")
  };
}