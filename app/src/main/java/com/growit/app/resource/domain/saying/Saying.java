package com.growit.app.resource.domain.saying;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Saying {
  private String id;
  private String message;
  private String author;

  public static Saying from(String id, String message, String author) {
    return Saying.builder().id(id).message(message).author(author).build();
  }
}
