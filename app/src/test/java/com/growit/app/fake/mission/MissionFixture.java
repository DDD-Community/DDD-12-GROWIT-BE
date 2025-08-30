package com.growit.app.fake.mission;

import com.growit.app.mission.domain.Mission;
import com.growit.app.mission.domain.vo.MissionType;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class MissionFixture {

  public static Mission defaultMission() {
    return new MissionBuilder().build();
  }

  public static Mission customMission(
      String id, String userId, String content, boolean finished, LocalDateTime createdAt) {
    MissionBuilder builder = new MissionBuilder();
    if (id != null) builder.id(id);
    if (userId != null) builder.userId(userId);
    if (content != null) builder.content(content);
    builder.finished(finished);
    if (createdAt != null) builder.createdAt(createdAt);

    return builder.build();
  }

  public static Mission missionWithId(String id) {
    return new MissionBuilder().id(id).build();
  }

  public static Mission missionWithUserId(String userId) {
    return new MissionBuilder().userId(userId).build();
  }

  public static Mission missionWithContent(String content) {
    return new MissionBuilder().content(content).build();
  }

  public static Mission missionWithFinished(boolean finished) {
    return new MissionBuilder().finished(finished).build();
  }

  public static Mission missionWithCreatedAt(LocalDateTime createdAt) {
    return new MissionBuilder().createdAt(createdAt).build();
  }

  public static Mission missionWithType(MissionType type) {
    return new MissionBuilder().type(type).build();
  }

  public static Mission missionWithDayOfWeek(DayOfWeek dayOfWeek) {
    return new MissionBuilder().dayOfWeek(dayOfWeek).build();
  }
}

class MissionBuilder {
  private String id = "mission-1";
  private String userId = "user-1";
  private String content = "오늘의 투두 작성하기";
  private boolean finished = false;
  private LocalDateTime createdAt = LocalDateTime.now();
  private DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
  private MissionType type = MissionType.DAILY_TODO_WRITE;

  public MissionBuilder id(String id) {
    this.id = id;
    return this;
  }

  public MissionBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public MissionBuilder content(String content) {
    this.content = content;
    return this;
  }

  public MissionBuilder finished(boolean finished) {
    this.finished = finished;
    return this;
  }

  public MissionBuilder createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public MissionBuilder dayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
    return this;
  }

  public MissionBuilder type(MissionType type) {
    this.type = type;
    return this;
  }

  public Mission build() {
    return Mission.builder()
        .id(id)
        .content(content)
        .finished(finished)
        .dayOfWeek(dayOfWeek)
        .type(type)
        .build();
  }
}
