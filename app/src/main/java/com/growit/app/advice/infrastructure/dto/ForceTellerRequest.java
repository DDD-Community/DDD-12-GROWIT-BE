package com.growit.app.advice.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ForceTellerRequest {
  private String name;
  private String gender; // "M" or "F"
  private String calendar; // "S" or "L"
  private String birthday; // "YYYY/MM/DD"
  private String birthtime; // "HH:mm"
  private boolean hmUnsure;
  private boolean midnightAdjust;
  private int year;
  private int month;
  private int day;
  private int hour;
  private int min;
}
