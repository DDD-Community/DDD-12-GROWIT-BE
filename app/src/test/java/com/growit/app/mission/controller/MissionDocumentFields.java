package com.growit.app.mission.controller;

import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;

public class MissionDocumentFields {
  public static final String TAG = "Mission";
  
  public static final FieldDescriptor[] MISSION_RESPONSE_FIELDS = {
    subsectionWithPath("data").description("미션 목록 배열"),
    subsectionWithPath("data[].id").description("미션 ID"),
    subsectionWithPath("data[].dayOfWeek").description("요일"),
    subsectionWithPath("data[].content").description("미션 내용"),
    subsectionWithPath("data[].type").description("미션 타입"),
    subsectionWithPath("data[].finished").description("완료 여부")
  };
}