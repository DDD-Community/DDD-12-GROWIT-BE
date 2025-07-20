package com.growit.app.resource.controller.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class SyncSayingsRequest {
  private List<SayingItem> sayings;

  @Getter
  public static class SayingItem {
    private String id;
    private String message;
    private String author;
  }
}
