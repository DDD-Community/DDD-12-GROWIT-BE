package com.growit.app.user.domain.user.vo;

public enum EarthlyBranchHour {
  JA("자시", "23:00", "01:30"),
  CHUK("축시", "01:30", "03:30"),
  IN("인시", "03:30", "05:30"),
  MYO("묘시", "05:30", "07:30"),
  JIN("진시", "07:30", "09:30"),
  SA("사시", "09:30", "11:30"),
  O("오시", "11:30", "13:30"),
  MI("미시", "13:30", "15:30"),
  SIN("신시", "15:30", "17:30"),
  YU("유시", "17:30", "19:30"),
  SUL("술시", "19:30", "21:30"),
  HAE("해시", "21:30", "23:30");

  private final String label;
  private final String start;
  private final String end;

  EarthlyBranchHour(String label, String start, String end) {
    this.label = label;
    this.start = start;
    this.end = end;
  }

  public String getLabel() {
    return label;
  }

  public String getStart() {
    return start;
  }

  public String getEnd() {
    return end;
  }

  public String toDisplayString() {
    return label + " " + start + " ~ " + end;
  }

  public static EarthlyBranchHour fromLabel(String label) {
    for (EarthlyBranchHour h : values()) {
      if (h.label.equals(label)) {
        return h;
      }
    }
    throw new IllegalArgumentException("Unknown label: " + label);
  }
}
