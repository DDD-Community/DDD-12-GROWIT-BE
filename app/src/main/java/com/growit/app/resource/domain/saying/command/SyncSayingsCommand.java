package com.growit.app.resource.domain.saying.command;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SyncSayingsCommand {
  private final List<SayingData> sayings;

  @Getter
  @AllArgsConstructor
  public static class SayingData {
    private final String id;
    private final String message;
    private final String author;
  }
}
